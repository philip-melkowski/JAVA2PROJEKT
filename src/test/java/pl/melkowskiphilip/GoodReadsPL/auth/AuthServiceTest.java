package pl.melkowskiphilip.GoodReadsPL.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.melkowskiphilip.GoodReadsPL.dto.LoginRequestDTO;
import pl.melkowskiphilip.GoodReadsPL.dto.LoginResponseDTO;
import pl.melkowskiphilip.GoodReadsPL.dto.UserRegisterDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.ActivationToken;
import pl.melkowskiphilip.GoodReadsPL.entity.Role;
import pl.melkowskiphilip.GoodReadsPL.entity.User;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.*;
import pl.melkowskiphilip.GoodReadsPL.mail.EmailService;
import pl.melkowskiphilip.GoodReadsPL.repository.ActivationTokenRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.UserRepository;
import pl.melkowskiphilip.GoodReadsPL.security.JWT.JWTService;
import pl.melkowskiphilip.GoodReadsPL.service.ActivationTokenService;
import pl.melkowskiphilip.GoodReadsPL.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private ActivationTokenService activationTokenService;

    @Mock
    private ActivationTokenRepository activationTokenRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    // =========================
    // LOGIN
    // =========================

    @Test
    void shouldLoginSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setUsername("user");
        user.setPassword("hashed");
        user.setEnabled(true);
        user.setRole(Role.USER);

        LoginRequestDTO request = new LoginRequestDTO("test@test.com", "password");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "hashed")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("JWT_TOKEN");

        LoginResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertEquals("JWT_TOKEN", response.getToken());
        assertEquals("user", response.getUsername());
        assertEquals(1L, response.getUserId());
        assertEquals(Role.USER, response.getRole());
    }

    @Test
    void shouldThrowWhenUserNotFoundOnLogin() {
        LoginRequestDTO request = new LoginRequestDTO("x@test.com", "pass");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> authService.login(request));
    }

    @Test
    void shouldThrowWhenPasswordInvalid() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("hashed");
        user.setEnabled(true);

        LoginRequestDTO request = new LoginRequestDTO("test@test.com", "wrong");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(request));
    }

    @Test
    void shouldResendActivationMailWhenAccountNotActivated() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setEnabled(false);

        ActivationToken token = new ActivationToken();
        token.setToken("ACTIVATION_TOKEN");

        LoginRequestDTO request = new LoginRequestDTO("test@test.com", "password");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(activationTokenRepository.findByUser(user)).thenReturn(Optional.of(token));
        when(activationTokenService.isTokenValid(token)).thenReturn(true);

        assertThrows(AccountNotActivatedException.class,
                () -> authService.login(request));

        verify(emailService, times(1))
                .sendActivationEmail(user, "ACTIVATION_TOKEN");
    }

    // =========================
    // REGISTER
    // =========================

    @Test
    void shouldRegisterUserSuccessfully() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setEmail("new@test.com");
        dto.setUsername("newUser");
        dto.setPassword("pass");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(dto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashed");
        when(activationTokenService.createTokenForUser(any())).thenReturn(new ActivationToken());

        authService.register(dto);

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendActivationEmail(any(), any());
    }

    @Test
    void shouldThrowWhenEmailAlreadyUsedOnRegister() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setEmail("used@test.com");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class,
                () -> authService.register(dto));
    }

    @Test
    void shouldThrowWhenUsernameAlreadyUsedOnRegister() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setEmail("ok@test.com");
        dto.setUsername("taken");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(dto.getUsername())).thenReturn(true);

        assertThrows(UsernameAlreadyUsedException.class,
                () -> authService.register(dto));
    }

    // =========================
    // RESEND ACTIVATION
    // =========================

    @Test
    void shouldResendActivationMail() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setEnabled(false);

        ActivationToken token = new ActivationToken();
        token.setToken("TOKEN");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(activationTokenRepository.findByUser(user)).thenReturn(Optional.of(token));
        when(activationTokenService.isTokenValid(token)).thenReturn(true);

        String response = authService.resendActivationMail(user.getEmail());

        assertNotNull(response);
        verify(emailService, times(1))
                .sendActivationEmail(user, "TOKEN");
    }

    @Test
    void shouldThrowWhenResendingForActivatedAccount() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setEnabled(true);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(AccountAlreadyActivatedException.class,
                () -> authService.resendActivationMail(user.getEmail()));
    }
}