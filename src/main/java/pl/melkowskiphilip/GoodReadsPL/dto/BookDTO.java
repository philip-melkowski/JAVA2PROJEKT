package pl.melkowskiphilip.GoodReadsPL.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.melkowskiphilip.GoodReadsPL.entity.Genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Year;

@Data // Z Lomboka – generuje automatycznie: gettery, settery, equals(),
// hashCode(), toString(). Nie musisz ich pisać ręcznie.
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long id;

    @Size(max = 200, message = "Książka musi mieć tytuł")
    @NotBlank
    private String title;

    // genre moze byc puste
    private Genre genre;

    // rok publikacji moze byc pusty
    private Year publishYear;

    @NotBlank
    private Long authorId;

    @Size(max = 50, message = "Imie autora może miec max 50 znaków długości")
    private String authorName;  // np. "J.K."

    @Size(max = 50, message = "Nazwisko autora może miec max 50 znaków długości")
    @NotBlank
    private String authorSurname; // np. " Rowling"
    private Double averageRating;
}