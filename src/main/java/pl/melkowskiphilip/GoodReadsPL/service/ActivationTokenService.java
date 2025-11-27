package pl.melkowskiphilip.GoodReadsPL.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.melkowskiphilip.GoodReadsPL.entity.ActivationToken;
import pl.melkowskiphilip.GoodReadsPL.entity.User;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.ActivationTokenExpiredException;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.InvalidActivationTokenException;
import pl.melkowskiphilip.GoodReadsPL.repository.ActivationTokenRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivationTokenService {

    private final ActivationTokenRepository activationTokenRepository;
    private final UserRepository userRepository; // do zapisania uzykotwnika do bazy



    // tworzenie tokenu dla użytkownika
    public ActivationToken createTokenForUser(User user)
    {
        ActivationToken token = new ActivationToken();

        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusDays(7));

        return activationTokenRepository.save(token);
    }


    // sprawdzenie ważności tokenu
    public boolean isTokenValid(ActivationToken token)
    {
        return token.getExpiresAt().isAfter(LocalDateTime.now());
    }

    // aktywacja konta - jeśli token istnieje i ważny
    public void activateAccount(String token)
    {
        ActivationToken optionalToken = activationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidActivationTokenException("Niepoprawny token aktywacji."));

        if(!isTokenValid(optionalToken))
        {
            throw new ActivationTokenExpiredException("Token wygasł!");
        }

        User user = optionalToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        // jak już zostanie użyty do aktywacji to usuń token z bazy
        activationTokenRepository.delete(optionalToken);
    }




}
