package pl.melkowskiphilip.GoodReadsPL.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.melkowskiphilip.GoodReadsPL.entity.ActivationToken;
import pl.melkowskiphilip.GoodReadsPL.repository.ActivationTokenRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.UserRepository;

import java.time.LocalDateTime;

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

    // testy


}
