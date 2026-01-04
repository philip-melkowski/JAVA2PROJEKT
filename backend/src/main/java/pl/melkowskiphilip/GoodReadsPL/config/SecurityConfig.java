package pl.melkowskiphilip.GoodReadsPL.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.melkowskiphilip.GoodReadsPL.security.filter.JWTAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final HandlerExceptionResolver handlerExceptionResolver;

    // 1. AuthenticationManager – potrzebny dla logowania (auth/login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // GŁÓWNA konfiguracja Spring Security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .csrf(AbstractHttpConfigurer::disable) // REST API → CSRF off
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT → bez sesji
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // na razie wszystko permit all
                        .anyRequest().permitAll()

                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, e) ->
                        handlerExceptionResolver.resolveException(req, res, null, e)
                ))
                .formLogin(AbstractHttpConfigurer::disable) // wylaczone bo domyslnie podmienia zadanie post na /login na formularz htttp - jeśli RESTOWA apka z Reactem to wyłączamy to
                .httpBasic(AbstractHttpConfigurer::disable); // przezytek do autoryzacji basic username:password


        // wpiecie filtra tokenem JWT przed AuthFilter bo wybierany jest na zasadzie kto pierwszy
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}