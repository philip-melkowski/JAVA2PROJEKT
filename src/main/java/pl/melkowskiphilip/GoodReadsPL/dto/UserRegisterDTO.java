package pl.melkowskiphilip.GoodReadsPL.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pl.melkowskiphilip.GoodReadsPL.validation.NotBlankTrimmed;

@Data
public class UserRegisterDTO {

    @NotBlankTrimmed(message = "Nazwa użytkownika nie może być pusta.")
    @Size(min = 3, max = 30, message = "Nazwa użytkownika musi mieć 3–30 znaków.")
    private String username;

    @NotBlankTrimmed(message = "Email nie może być pusty.")
    @Email(message = "Email ma niepoprawny format.")
    private String email;

    @NotBlankTrimmed(message = "Hasło nie może być puste.")
    @Size(min = 8, max = 64, message = "Hasło musi mieć co najmniej 8 znaków.")
    private String password;
}