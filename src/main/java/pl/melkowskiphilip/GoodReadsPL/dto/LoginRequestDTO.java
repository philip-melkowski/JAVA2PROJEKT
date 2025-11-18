package pl.melkowskiphilip.GoodReadsPL.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginRequestDTO {


    @NotBlank(message = "Musisz podać adres email")
    @Email(message = "Podaj poprawny adres email.")
    private String email;

    @NotBlank(message = "Musisz podać hasło, by sie zalogować")
    @Size(min = 8, max = 64, message = "Hasło musi mieć co najmniej 8 znaków.")
    private String password;
}
