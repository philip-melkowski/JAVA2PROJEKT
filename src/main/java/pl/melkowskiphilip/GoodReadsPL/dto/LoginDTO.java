package pl.melkowskiphilip.GoodReadsPL.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "Nazwa użytkownika lub email jest wymagany.")
    private String usernameOrEmail;

    @NotBlank(message = "Hasło jest wymagane.")
    private String password;
}