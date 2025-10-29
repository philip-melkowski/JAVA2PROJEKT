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

    //  Pobranie wszystkich książek
    public List<BookDTO> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    //  Pobranie książki po ID
    public BookDTO findById(Long id) {
        return bookRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    //  Zapis nowej książki
    @Transactional // odczyt i zapis zmian
    public BookDTO saveFromDTO(BookDTO dto) {
        boolean exists = bookRepository.existsByTitleAndAuthorId(dto.getTitle(), dto.getAuthorId());
        if(exists)
        {
            throw new IllegalArgumentException("Ksiazka juz istnieje w bazie");
        }
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

    //  Usunięcie książki po ID
    @Transactional // odczyt i zapis zmian
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    // 🔹 Sortowanie malejąco po średniej (najlepsze książki)
    public List<BookDTO> sortBooksByAverageRatingDesc(List<BookDTO> books) {
        return books.stream()
                .sorted((b1, b2) -> Double.compare(
                        b2.getAverageRating() != null ? b2.getAverageRating() : 0.0,
                        b1.getAverageRating() != null ? b1.getAverageRating() : 0.0))
                .collect(Collectors.toList());
    }

    // 🔹 Sortowanie rosnąco po średniej (najgorsze książki)
    public List<BookDTO> sortBooksByAverageRatingAsc(List<BookDTO> books) {
        return books.stream()
                .sorted((b1, b2) -> Double.compare(
                        b1.getAverageRating() != null ? b1.getAverageRating() : 0.0,
                        b2.getAverageRating() != null ? b2.getAverageRating() : 0.0))
                .collect(Collectors.toList());
    }


    // aktualizacja ksiazki
    @Transactional // do odczytu i zapisu
    public BookDTO updateBook(Long id, BookDTO updatedBook)
    {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono ksiazki o ID " + id));
        book.setTitle(updatedBook.getTitle());
        book.setGenre(updatedBook.getGenre());
        book.setPublishYear(updatedBook.getPublishYear());

        var author = authorRepository.findById(updatedBook.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono autora o ID " + updatedBook.getAuthorId()));
        book.setAuthor(author);

        return toDTO(bookRepository.save(book));
    }

    //  Mapowanie Book -> BookDTO
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
        dto.setAverageRating(bookRepository.findAverageRatingForBook(book.getId()));
        return dto;
    }

}
