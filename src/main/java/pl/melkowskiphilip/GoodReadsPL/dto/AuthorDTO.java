package pl.melkowskiphilip.GoodReadsPL.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data // Z Lomboka – generuje automatycznie: gettery, settery, equals(),
// hashCode(), toString(). Nie musisz ich pisać ręcznie.
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {

    // przy ID nie dawac adnotacji @NotNull - bo przy POST to pole bedzie zawsze NULL
    private Long id;
    @Size(max = 50, message = "Imie autora nie może byc dłuższe niż 50 znaków")
    private String name;
    @Size(max = 50, message = "Nazwisko autora nie może byc dłuższe niż 50 znaków")
    @NotBlank(message = "Nazwisko autora nie może byc puste")
    private String surname;
}
