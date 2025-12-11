package pl.melkowskiphilip.GoodReadsPL.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import pl.melkowskiphilip.GoodReadsPL.validation.NotBlankTrimmed;

@Data
public class ChangePasswordDTO {

    @NotBlankTrimmed(message = "Aktualne hasło jest wymagane.")
    private String oldPassword;

    @NotBlankTrimmed(message = "Nowe hasło jest wymagane.")
    @Size(min = 8, max = 64, message = "Hasło musi mieć co najmniej 8 znaków.")
    private String newPassword;
}