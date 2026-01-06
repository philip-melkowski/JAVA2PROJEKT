package pl.melkowskiphilip.GoodReadsPL.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.melkowskiphilip.GoodReadsPL.dto.*;


import java.util.Map;


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

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getMe(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return ResponseEntity.ok(Map.of("username", userDetails.getUsername()));


    }
}
