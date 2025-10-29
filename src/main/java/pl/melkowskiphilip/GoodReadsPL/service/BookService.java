package pl.melkowskiphilip.GoodReadsPL.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.melkowskiphilip.GoodReadsPL.entity.Book;
import pl.melkowskiphilip.GoodReadsPL.entity.Genre;
import pl.melkowskiphilip.GoodReadsPL.repository.AuthorRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.BookRepository;

import pl.melkowskiphilip.GoodReadsPL.dto.BookDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // tutaj metody tylko do odczytu, chyba ze maja wlasną adnotacje @transactional. poprawia wydajność
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository; // do pobrania autora po authorId i przypisania go do ksiazki;

    // 🔹 Pobranie wszystkich książek
    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Pobranie książki po ID
    public BookDTO findById(Long id) {
        return bookRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    // 🔹 Pobranie książki po tytule (dokładna nazwa)
    public BookDTO findByTitle(String title) {
        return bookRepository.findByTitleIgnoreCase(title)
                .map(this::toDTO)
                .orElse(null);
    }

    // 🔹 Wyszukiwanie książek po fragmencie tytułu
    public List<BookDTO> searchByTitle(String part) {
        return bookRepository.findByTitleContainingIgnoreCase(part)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Pobranie książek danego autora
    public List<BookDTO> findByAuthor(Long authorId) {
        return bookRepository.findAllByAuthorId(authorId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Pobranie książek danego gatunku
    public List<BookDTO> findByGenre(Genre genre) {
        return bookRepository.findAllByGenre(genre)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Sprawdzenie, czy książka już istnieje
    public boolean existsByTitleAndAuthor(String title, Long authorId) {
        return bookRepository.existsByTitleAndAuthorId(title, authorId);
    }

    // 🔹 Zapis nowej książki
    @Transactional // odczyt i zapis
    public BookDTO saveFromDTO(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setGenre(dto.getGenre());
        book.setPublishYear(dto.getPublishYear());
        var author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono autora o ID " + dto.getAuthorId()));
        book.setAuthor(author);
        Book saved = bookRepository.save(book);

        return toDTO(saved);
    }

    // 🔹 Usunięcie książki po ID
    @Transactional // odczyt i zapis
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    // 🔹 Średnia ocen książki
    public Double getAverageRating(Long bookId) {
        return bookRepository.findAverageRatingForBook(bookId);
    }

    // 🔹 Rozkład ocen (ile ocen 1, 2, 3, itd.)
    public List<Object[]> getRatingDistribution(Long bookId) {
        return bookRepository.findRatingDistributionForBook(bookId);
    }

    // 🔹 Książki z najwyższą średnią
    public List<BookDTO> getTopRatedBooks() {
        return bookRepository.findTopRatedBooks()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Książki z najniższą średnią
    public List<BookDTO> getBottomRatedBooks() {
        return bookRepository.findBottomRatedBooks()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Książki powyżej określonej średniej ocen
    public List<BookDTO> findBooksAboveRating(double rating) {
        return bookRepository.findAllWithAverageRatingGreaterThan(rating)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Książki poniżej określonej średniej ocen
    public List<BookDTO> findBooksBelowRating(double rating) {
        return bookRepository.findAllWithAverageRatingLessThan(rating)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Książki danego gatunku powyżej określonej średniej ocen
    public List<BookDTO> findBooksByGenreAboveRating(Genre genre, double rating) {
        return bookRepository.findAllByGenreWithAverageRatingGreaterThan(genre, rating)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Książki danego gatunku poniżej określonej średniej ocen
    public List<BookDTO> findBooksByGenreBelowRating(Genre genre, double rating) {
        return bookRepository.findAllByGenreWithAverageRatingLessThan(genre, rating)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Książki danego autora powyżej określonej średniej ocen
    public List<BookDTO> findBooksByAuthorAboveRating(Long authorId, double rating) {
        return bookRepository.findBooksByAuthorWithAvgRatingGreaterThan(authorId, rating)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Książki danego autora poniżej określonej średniej ocen
    public List<BookDTO> findBooksByAuthorBelowRating(Long authorId, double rating) {
        return bookRepository.findBooksByAuthorWithAvgRatingLessThan(authorId, rating)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Mapowanie Book -> BookDTO
    private BookDTO toDTO(Book book) {
        if (book == null) return null;
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setGenre(book.getGenre());
        dto.setPublishYear(book.getPublishYear());
        dto.setAuthorName(book.getAuthor().getName());
        dto.setAuthorSurname(book.getAuthor().getSurname());
        dto.setAuthorId(book.getAuthor().getId());
        return dto;
    }
}
