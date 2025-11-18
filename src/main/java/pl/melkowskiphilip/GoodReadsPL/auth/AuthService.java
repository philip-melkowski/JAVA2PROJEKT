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
import pl.melkowskiphilip.GoodReadsPL.entity.Role;
import pl.melkowskiphilip.GoodReadsPL.entity.User;
import pl.melkowskiphilip.GoodReadsPL.repository.UserRepository;
import pl.melkowskiphilip.GoodReadsPL.security.JWT.JWTService;
import pl.melkowskiphilip.GoodReadsPL.service.UserService;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UserService userService;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user =
                userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownik o podanym emailu nie istnieje"));

        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Niepoprawne dane logowania");
        }
        if(!user.isEnabled()) {
            throw new DisabledException("Konto nie jest aktywne. Aktywuj na mailu.");
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
        return userService.toDTO(newUser);
    }



}
