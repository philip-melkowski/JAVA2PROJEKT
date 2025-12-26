package pl.melkowskiphilip.GoodReadsPL.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.melkowskiphilip.GoodReadsPL.validation.NotBlankTrimmed;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {


    @NotBlankTrimmed(message = "Musisz podać adres email")
    @Email(message = "Podaj poprawny adres email.")
    private String email;

    @NotBlankTrimmed(message = "Musisz podać hasło, by sie zalogować")
    @Size(min = 8, max = 64, message = "Hasło musi mieć co najmniej 8 znaków.")
    private String password;
}
