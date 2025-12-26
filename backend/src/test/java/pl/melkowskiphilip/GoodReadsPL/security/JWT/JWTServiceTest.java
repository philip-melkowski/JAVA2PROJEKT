package pl.melkowskiphilip.GoodReadsPL.security.JWT;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setup() {
        jwtService = new JWTService();

        // ręcznie ustawiamy wartości z application.properties
        jwtService.expirationJWT = 1000L * 60 * 60; // 1 godzina

        // klucz MUSI mieć odpowiednią długość (BASE64!)
        jwtService.secretKey =
                "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";

        userDetails = User
                .withUsername("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
    }

    /* =========================
       generateToken()
       ========================= */

    @Test
    void shouldGenerateToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    /* =========================
       extractUsername()
       ========================= */

    @Test
    void shouldExtractUsernameFromToken() {
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertEquals("testuser", username);
    }

    /* =========================
       extractExpiration()
       ========================= */

    @Test
    void shouldExtractExpirationDate() {
        String token = jwtService.generateToken(userDetails);

        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    /* =========================
       generateToken with extra claims
       ========================= */

    @Test
    void shouldGenerateTokenWithExtraClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "USER");

        String token = jwtService.generateToken(extraClaims, userDetails);

        Claims claims = jwtService.extractAllClaims(token);

        assertEquals("USER", claims.get("role"));
        assertEquals("testuser", claims.getSubject());
    }

    /* =========================
       isTokenExpired()
       ========================= */

    @Test
    void shouldReturnFalseForFreshToken() {
        String token = jwtService.generateToken(userDetails);

        boolean expired = jwtService.isTokenExpired(token);

        assertFalse(expired);
    }

    @Test
    void shouldReturnTrueForExpiredToken() throws InterruptedException {
        jwtService.expirationJWT = 1L; // 1 ms

        String token = jwtService.generateToken(userDetails);

        Thread.sleep(5); // dajemy czas na wygaśnięcie

        assertTrue(jwtService.isTokenExpired(token));
    }

    /* =========================
       isTokenValid()
       ========================= */

    @Test
    void shouldReturnTrueForValidTokenAndUser() {
        String token = jwtService.generateToken(userDetails);

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseForTokenWithDifferentUser() {
        String token = jwtService.generateToken(userDetails);

        UserDetails otherUser = User
                .withUsername("otheruser")
                .password("password")
                .authorities("ROLE_USER")
                .build();

        boolean valid = jwtService.isTokenValid(token, otherUser);

        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseForExpiredToken() throws InterruptedException {
        jwtService.expirationJWT = 1L;

        String token = jwtService.generateToken(userDetails);
        Thread.sleep(5);

        assertFalse(jwtService.isTokenValid(token, userDetails));
    }
}