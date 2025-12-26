package pl.melkowskiphilip.GoodReadsPL.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.melkowskiphilip.GoodReadsPL.dto.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // logowanie
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
        return ResponseEntity.ok(loginResponseDTO);
    }

    // rejestracja
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegisterDTO user) {
        UserDTO registeredUser = authService.register(user);
        return ResponseEntity.ok(registeredUser);
    }


    // ponowne wysłanie maila aktywującego konto
    @PostMapping("/resendActivationMail")
    public ResponseEntity<String> resendActivationMail(@Valid @RequestBody ResendActivationDTO mail) {
        return ResponseEntity.ok(authService.resendActivationMail(mail.getEmail()));
    }
}
