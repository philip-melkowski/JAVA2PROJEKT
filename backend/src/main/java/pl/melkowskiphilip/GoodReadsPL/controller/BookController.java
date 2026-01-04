package pl.melkowskiphilip.GoodReadsPL.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.melkowskiphilip.GoodReadsPL.dto.BookDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.Genre;
import pl.melkowskiphilip.GoodReadsPL.service.BookService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /* =========================================================
       =============== DEPRECATED – NIE UŻYWAĆ =================
       =========================================================

    // zwraca wszystkie książki (bez paginacji – NIEOPTYMALNE)
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {

        return ResponseEntity.ok(bookService.getAllBooksSorted(sortBy, order));
    }

    // endpoint na daną stronę (bez filtrów)
    @GetMapping("/page")
    public ResponseEntity<Page<BookDTO>> getBooksPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(bookService.getPage(page, size));
    }

    // filtrowanie i sortowanie po stronie backendu + streamy (NIEOPTYMALNE)
    @GetMapping("/filter")
    public ResponseEntity<List<BookDTO>> filterAndSortBooks(
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorPart,
            @RequestParam(defaultValue = "desc") String order) {

        // stara implementacja – usunięta logicznie
        return ResponseEntity.badRequest().build();
    }

    ========================================================= */

    /* GŁÓWNY ENDPOINT DO FRONTENDU
      - paginacja
      - sortowanie
      - filtrowanie
      - wszystko po BACKENDZIE (DB)

      Przykład:
        /api/books/search?page=0&size=10&sortBy=title&order=asc&genre=FANTASY
     */
    @GetMapping("/search")
    public ResponseEntity<Page<BookDTO>> searchBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String title,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                bookService.searchBooks(page, size, sortBy, order, genre, authorId, title)
        );
    }

    // CRUD


    // zwraca książkę po ID
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    // dodaje książkę
    @PostMapping
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.saveFromDTO(bookDTO));
    }

    // aktualizacja książki
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO updatedBook
    ) {
        return ResponseEntity.ok(bookService.updateBook(id, updatedBook));
    }

    // usuwa książkę
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}