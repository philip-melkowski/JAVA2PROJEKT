package pl.melkowskiphilip.GoodReadsPL.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import pl.melkowskiphilip.GoodReadsPL.security.JWT.JWTService;
import pl.melkowskiphilip.GoodReadsPL.security.UserDetailsServiceImpl;

import java.io.IOException;


/*
filtr:
	1.	Sprawdza czy jest token
	2.	Wyciąga username
	3.	Sprawdza czy user nie jest już zalogowany
	4.	Pobiera usera z DB
	5.	Weryfikuje token (ważność, podpis, subject)
	6.	Jeśli OK → zaloguj usera w Spring Security
	7.	Przepuść request dalej
 */


@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    // w controllerach domyslnie bledy sa zwracane jako odpowiedz http, ale JWT to jest warstwa poza controllerem wiec potrzebuje JWT handlera zeby moc zwracac bledy jako odpowiedzi http
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JWTService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public JWTAuthenticationFilter(JWTService jwtService, UserDetailsServiceImpl userDetailsService, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")) {

            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        // sprawdzamy czy naglowek obecny i czy w formie Bearer
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            // jesli nie to: przepuszczamy zadanie dalej
            filterChain.doFilter(request, response);
            return;
        }

        try
        {
            // wyciagniecie tokenu z naglowka
            final String jwt = authHeader.substring(7);
            // wyciagniecie username z tokenu
            final String username = jwtService.extractUsername(jwt);

            // Pobranie obecnej autentykacji z kontekstu
            // Spring Security trzyma aktualnie zalogowanego usera w: SecurityContextHolder (thread-local storage)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // jesli username nie jest nullem i jesli nie jest obecnie zalogowany to jest auth == null
            if(username != null && authentication == null) {

                // pobranie userDetails z bazy
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // jesli token jest valid (subject z tokena = username z userDetails) i nie jest przedawniony
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    // logowanie uzytkownika w spring security
                    // userDetails - dane uzytkowniak do logowanie, null - haslo jest null bo logujemy sie tokenem, getAuthorities() - role uzytkowniak
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // od tego momentu zadanie jest uznane za "zalogowane"
                    authToken.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
                else
                {
                    // jesli username to null lub jestesmy obecnie juz zalogowani to zwroc ten blad.
                    // po co? Twój filtr wpuszcza dalej nieautoryzowane żądania z nielegalnym tokenem, podczas gdy powinien przerwać łańcuch i zwrócić 401 Unauthorized.
                    /*
                    przyklad:
                    Fałszywy token do endpointu „tylko dla admina”:
                        GET /api/admin/stats
                        Authorization: Bearer FAKE
                     bez tego exception zwrociloby:
                        403 Forbidden
                     co daje atakujacemu informacje ze istnieje taki endpoint i ze problem w autoryzacji
                     powinno byc:
                        401 Unauthorized
                     dzieki obecnemu rozwiazaniu tak jest
                     */
                    throw new BadCredentialsException("Niepoprawny lub przedawniony token!");
                }


            }

            // puszcza dalej zadanie, jesli nie puscimy to tu utknie
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
        {"message":"Invalid or expired token"}
    """);
            return;
        }

    }
}
