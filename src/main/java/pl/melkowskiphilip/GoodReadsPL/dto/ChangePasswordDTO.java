package pl.melkowskiphilip.GoodReadsPL.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDTO {

    @NotBlank(message = "Aktualne hasło jest wymagane.")
    private String oldPassword;

    @NotBlank(message = "Nowe hasło jest wymagane.")
    @Size(min = 8, max = 64, message = "Hasło musi mieć co najmniej 8 znaków.")
    private String newPassword;
}