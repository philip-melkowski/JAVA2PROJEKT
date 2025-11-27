package pl.melkowskiphilip.GoodReadsPL.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.melkowskiphilip.GoodReadsPL.dto.LoginRequestDTO;
import pl.melkowskiphilip.GoodReadsPL.dto.LoginResponseDTO;
import pl.melkowskiphilip.GoodReadsPL.dto.UserDTO;
import pl.melkowskiphilip.GoodReadsPL.dto.UserRegisterDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.ActivationToken;
import pl.melkowskiphilip.GoodReadsPL.entity.Role;
import pl.melkowskiphilip.GoodReadsPL.entity.User;
import pl.melkowskiphilip.GoodReadsPL.mail.EmailService;
import pl.melkowskiphilip.GoodReadsPL.repository.ActivationTokenRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.UserRepository;
import pl.melkowskiphilip.GoodReadsPL.security.JWT.JWTService;
import pl.melkowskiphilip.GoodReadsPL.service.ActivationTokenService;
import pl.melkowskiphilip.GoodReadsPL.service.UserService;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UserService userService;
    private final ActivationTokenService activationTokenService;
    private final ActivationTokenRepository activationTokenRepository;
    private final EmailService emailService;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user =
                userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownik o podanym emailu nie istnieje"));

        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Niepoprawne dane logowania"); // tutaj dodac w drugim jezyku
        }
        // jesli nie zostalo aktywowane konto
        if(!user.isEnabled()) {
            ActivationToken existing = activationTokenRepository.findByUser(user).orElse(null);
            ActivationToken token;
            // jesli token wciaz aktywny to wyslij go
            if(existing != null && activationTokenService.isTokenValid(existing)) {
                token = existing;
            }
            // jesli nie ist lub nieaktynwy juz - stworz nowy token
            else
            {
                if(existing != null) {
                    activationTokenRepository.delete(existing);
                }
                token = activationTokenService.createTokenForUser(user);
            }
            emailService.sendActivationEmail(user, token.getToken());

            throw new DisabledException("Konto nieaktywne - wysłaliśmy mail z linkiem aktywacji");


        }
        String token = jwtService.generateToken(user);
        return new LoginResponseDTO(
                token,
                user.getUsername(),
                user.getId(),
                user.getRole());

    }

    public UserDTO register(UserRegisterDTO user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Istnieje już konto z podanym adresem e-mail");
        }
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalStateException("Istnieje już konto z podaną nazwą użytkownika");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEnabled(false);
        newUser.setRole(Role.USER);
        userRepository.save(newUser);

        ActivationToken token = activationTokenService.createTokenForUser(newUser);
        emailService.sendActivationEmail(newUser, token.getToken());


        return userService.toDTO(newUser);
    }



}
