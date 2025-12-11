package pl.melkowskiphilip.GoodReadsPL.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.melkowskiphilip.GoodReadsPL.dto.BookDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.Genre;
import pl.melkowskiphilip.GoodReadsPL.service.BookService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // zwraca wszystkie ksiazki
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {

        return ResponseEntity.ok(bookService.getAllBooksSorted(sortBy, order));
    }

    // zwraca ksiazke po id
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    // endpoint na daną stronę
    @GetMapping("/page")
    public ResponseEntity<Page<BookDTO>> getBooksPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(bookService.getPage(page, size));
    }

    // dodaje ksiazki, jesli nie istnieje jeszce
    @PostMapping
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.saveFromDTO(bookDTO));
    }

    // usuwa ksiazke
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // elastyczny endpoint pozwalajacy filtrowac po wielu rzeczach na raz i sortowane te przefiltrowane dane
    @GetMapping("/filter")
    public ResponseEntity<List<BookDTO>> filterAndSortBooks(
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorPart,
            @RequestParam(defaultValue = "desc") String order) {

        // 1️⃣ pobierz bazową listę książek (bez filtrowania)
        List<BookDTO> books = bookService.findAll();

        // 2️⃣ filtruj krok po kroku
        if (genre != null)
            books = books.stream()
                    .filter(b -> b.getGenre() == genre)
                    .collect(Collectors.toList());

        if (authorId != null)
            books = books.stream()
                    .filter(b -> b.getAuthorId().equals(authorId))
                    .collect(Collectors.toList());

        if (title != null && !title.isBlank())
            books = books.stream()
                    .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .collect(Collectors.toList());

        if (authorPart != null && !authorPart.isBlank())
            books = books.stream()
                    .filter(b -> (b.getAuthorName() + " " + b.getAuthorSurname())
                            .toLowerCase().contains(authorPart.toLowerCase()))
                    .collect(Collectors.toList());

        // 3️⃣ sortowanie po średniej ocenie
        books = order.equalsIgnoreCase("asc")
                ? bookService.sortBooksByAverageRatingAsc(books)
                : bookService.sortBooksByAverageRatingDesc(books);

        return ResponseEntity.ok(books);
    }

    // aktualizacja ksiazki
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO updatedBook) {
        return ResponseEntity.ok(bookService.updateBook(id, updatedBook));
    }






}