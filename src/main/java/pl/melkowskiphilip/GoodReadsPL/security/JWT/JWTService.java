package pl.melkowskiphilip.GoodReadsPL.security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    protected String secretKey;

    @Value("${jwt.expiration}")
    @Getter
    protected Long expirationJWT;


    // wydobywa username z token - poniewaz username w naszym tokenie jest subjectem - czyli forma ID
    // oddelegowuje do extractClaims
    // Claims::getSubject to to samo co z Claims wywolaj  metode getSubject
    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    // z mapy wszystkich claimow wyciagnij jeden konkretny
    // claimsResolver przyjmuje typ Claims a zwraca typ T.
    // poprzez apply podajemy argument do claimsResolver
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // generowanie tokena bez custom claimow
    public String generateToken(UserDetails userDetails)
    {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Generowanie tokenu z dodatkowymi claimami.
    // Jest to przeciążona wersja metody, która:
    // 1) pozwala dołożyć custom claimy (np. userId, email, role) – trafiają one do payloadu JWT,
    // 2) używa domyślnego czasu wygasania expirationJWT,
    // 3) deleguje całą budowę tokena do metody buildToken(...).
    //
    // Ta wersja przydaje się, gdy chcemy przekazać do tokena więcej danych o użytkowniku
    // bez konieczności wykonywania dodatkowych requestów do bazy.
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails)
    {
        return buildToken(extraClaims, userDetails, expirationJWT);
    }

    // buduje token
    // mapa extraClaims to claimy inne niz standardowe (Subject, issued at, expired at itp)
    // setSubject, setIssuedAt itp ustawiaja standartdowe claimy, setClaims - pozostale
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, Long expiration)
    {
        return
                Jwts
                        .builder()
                        .setClaims(extraClaims)
                        .setSubject(userDetails.getUsername())
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + expiration))
                        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                        .compact();
    }

    // czy token jest prawidlowy - czy username == subject z tokenu
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        }
        catch (io.jsonwebtoken.ExpiredJwtException e) {
            // token wygasł → normalny stan, NIE wyjątek
            return false;
        }
    }

    // czy token przedawniony.
    public boolean isTokenExpired(String token)
    {
        try {
            return extractExpiration(token).before(new Date());
        }
        catch (io.jsonwebtoken.ExpiredJwtException e) {
            return true;
        }
    }


    // wyciagnij z mapy wszystkich claimow expiration
    public Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    // zwraca wszystkie "CLAIMY" - atrybuty klucza
    // uzywa parsera Jwts.
    public Claims extractAllClaims(String token)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // dekoduje klucz z BASE64 na key
    private Key getSignInKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
