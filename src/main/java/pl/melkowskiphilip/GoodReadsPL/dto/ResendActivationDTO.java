package pl.melkowskiphilip.GoodReadsPL.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendActivationDTO {

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Podaj poprawny adres email")
    private String email;

}
