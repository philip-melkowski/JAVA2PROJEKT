package pl.melkowskiphilip.GoodReadsPL.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.melkowskiphilip.GoodReadsPL.entity.Genre;

import jakarta.validation.constraints.Size;
import pl.melkowskiphilip.GoodReadsPL.validation.NotBlankTrimmed;

import java.time.Year;

@Data // Z Lomboka – generuje automatycznie: gettery, settery, equals(),
// hashCode(), toString(). Nie trzeba ich pisać ręcznie.
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    // przy ID nie dawac adnotacji @NotNull - bo przy POST to pole bedzie zawsze NULL
    private Long id;

    @Size(max = 200, message = "Tytuł może mieć max 200 znaków długości")
    @NotBlankTrimmed(message = "Książka musi mieć tytuł")
    private String title;

    // genre moze byc puste
    private Genre genre;

    // rok publikacji moze byc pusty
    private Year publishYear;

    @NotNull(message = "musi byc podane ID autora")
    private Long authorId;

    @Size(max = 50, message = "Imie autora może miec max 50 znaków długości")
    private String authorName;  // np. "J.K."

    @Size(max = 50, message = "Nazwisko autora może miec max 50 znaków długości")
    private String authorSurname; // np. " Rowling"
    private Double averageRating;
}