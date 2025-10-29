package pl.melkowskiphilip.GoodReadsPL.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Z Lomboka – generuje automatycznie: gettery, settery, equals(),
// hashCode(), toString(). Nie musisz ich pisać ręcznie.
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {

    private Long id;
    private String name;
    private String surname;
}
