package pl.melkowskiphilip.GoodReadsPL.service;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.melkowskiphilip.GoodReadsPL.entity.ActivationToken;
import pl.melkowskiphilip.GoodReadsPL.entity.User;
import pl.melkowskiphilip.GoodReadsPL.repository.ActivationTokenRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.UserRepository;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivationTokenServiceTest {

    @Mock
    private ActivationTokenRepository activationTokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ActivationTokenService activationTokenService;


    // testy dla isTokenValid
    @Test
    void shouldReturnTrueWhenTokenIsNotExpired() {

        //arrange - przygotuj dane
        ActivationToken token = new ActivationToken();
        token.setExpiresAt(LocalDateTime.now().plusDays(1));

        // act
        boolean result = activationTokenService.isTokenValid(token);

        // assert
        assert(result);
    }

    @Test
    void shouldReturnFalseWhenTokenIsExpired() {
        ActivationToken token = new ActivationToken();
        token.setExpiresAt(LocalDateTime.now().minusDays(1));

        boolean result = activationTokenService.isTokenValid(token);

        assert(!result);
    }

    @Test
    void shouldReturnFalseWhenTokenExpiresExactlyNow()
    {
        ActivationToken token = new ActivationToken();
        token.setExpiresAt(LocalDateTime.now());

        boolean result = activationTokenService.isTokenValid(token);

        assert(!result);
    }

    // koniec testow isTokenValid()


    // testy dla createTokenForUser()

    @Test
    void shouldCreateTokenForUser()
    {
        // arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("example@example.com");


        // przygotowanie tokenu
        ActivationToken savedToken = new ActivationToken();
        savedToken.setToken("UUID");
        savedToken.setUser(user);
        savedToken.setCreatedAt(LocalDateTime.now());
        savedToken.setExpiresAt(LocalDateTime.now().plusDays(7));

        // obsługa funkcji save w atrapie repo
        // jeśli wywolane zostanie save w repo to niech atrapa zwroci savedToken
        // potrzebne, bo createTokenForUser zwraca repo.save()
        when(activationTokenRepository.save(any(ActivationToken.class)))
                .thenReturn(savedToken);

        ActivationToken result = activationTokenService.createTokenForUser(user);

        assert result.getToken() != null;
        assert result.getUser().getId().equals(1L);
        assert result.getCreatedAt() != null;
        assert result.getExpiresAt().equals(result.getCreatedAt().plusDays(7));


        // sprawdzamy, czy save zostało wywołane raz
        verify(activationTokenRepository, times(1)).save(any(ActivationToken.class));



    }



}
