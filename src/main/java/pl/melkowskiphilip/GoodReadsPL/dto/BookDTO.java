package pl.melkowskiphilip.GoodReadsPL.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.melkowskiphilip.GoodReadsPL.entity.Genre;

import java.time.Year;

@Data // Z Lomboka – generuje automatycznie: gettery, settery, equals(),
// hashCode(), toString(). Nie musisz ich pisać ręcznie.
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private Genre genre;
    private Year publishYear;
    private Long authorId;
    private String authorName;  // np. "J.K."
    private String authorSurname; // np. " Rowling"
    private Double averageRating;
}