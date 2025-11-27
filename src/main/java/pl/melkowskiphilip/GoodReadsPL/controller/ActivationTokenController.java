package pl.melkowskiphilip.GoodReadsPL.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.melkowskiphilip.GoodReadsPL.service.ActivationTokenService;

@RestController
@RequestMapping("/api/auth/activation")
@RequiredArgsConstructor
public class ActivationTokenController {

    private final ActivationTokenService activationTokenService;

    @GetMapping
    public ResponseEntity<String> activateAccount(@RequestParam String token) {
        activationTokenService.activateAccount(token);
        return ResponseEntity.ok("Konto zostało pomyślnie aktywowane!");
    }
}
