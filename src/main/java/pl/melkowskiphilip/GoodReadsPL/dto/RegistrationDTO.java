package pl.melkowskiphilip.GoodReadsPL.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDTO {

    @NotBlank(message = "Nazwa użytkownika nie może być pusta.")
    private String username;

    @Email(message = "Email musi być poprawny.")
    @NotBlank(message = "Email nie może być pusty.")
    private String email;

    @NotBlank(message = "Hasło nie może być puste.")
    @Size(min = 8, message = "Hasło musi mieć co najmniej 8 znaków.")
    private String password;
}