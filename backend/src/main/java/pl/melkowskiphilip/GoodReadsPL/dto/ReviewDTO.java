package pl.melkowskiphilip.GoodReadsPL.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    private Long id;

    @Min(value = 1, message = "Ocena musi być co najmniej 1")
    @Max(value = 10, message = "Ocena nie może być większa niż 10")
    private int rating;

    @Size(max = 2000)
    private String comment;

    @NotNull
    private Long bookId;

    @NotNull
    private Long userId;

    // te pola bez @NotBlank, bo przy tworzeniu recenzji np. będą podawane tylko pola - userId, bookId
    // a nie nazwa uzytkownika czy tytul ksiazki
    private String username;   // nazwa użytkownika, jeśli chcesz pokazywać kto dodał recenzję
    private String bookTitle;  // tytuł książki, jeśli chcesz pokazywać przy recenzjach
}
