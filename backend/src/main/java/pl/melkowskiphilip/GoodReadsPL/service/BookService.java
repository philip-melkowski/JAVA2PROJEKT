package pl.melkowskiphilip.GoodReadsPL.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.melkowskiphilip.GoodReadsPL.dto.BookDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.Book;
import pl.melkowskiphilip.GoodReadsPL.entity.Genre;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.AuthorNotFoundException;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.BookAlreadyExistsException;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.BookNotFoundException;
import pl.melkowskiphilip.GoodReadsPL.repository.AuthorRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.BookRepository;
import pl.melkowskiphilip.GoodReadsPL.specification.BookSpecification;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    /* =========================================================
       =============== DEPRECATED – NIE UŻYWANE =================
       =========================================================

    //  Pobranie wszystkich książek
    public List<BookDTO> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    //  Pobranie książek z sortowaniem
    public List<BookDTO> getAllBooksSorted(String sortBy, String order) {
        Sort sort = order.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return bookRepository.findAll(sort)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    //  Paginacja bez filtrów
    public Page<BookDTO> getPage(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size))
                .map(this::toDTO);
    }

    ========================================================= */

    /**
     * GŁÓWNA metoda:
     * - paginacja
     * - sortowanie
     * - filtrowanie
     * wszystko po BACKENDZIE
     */
    public Page<BookDTO> searchBooks(
            int page,
            int size,
            String sortBy,
            String order,
            Genre genre,
            Long authorId,
            String title
    ) {
        // 🔒 zabezpieczenie – averageRating NIE jest polem encji
        if ("averageRating".equals(sortBy)) {
            sortBy = "title";
        }

        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        var specification = BookSpecification
                .hasGenre(genre)
                .and(BookSpecification.hasAuthor(authorId))
                .and(BookSpecification.titleContains(title));

        return bookRepository.findAll(specification, pageable)
                .map(this::toDTO);
    }



    // CRUD


    public BookDTO findById(Long id) {
        return bookRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new BookNotFoundException("error.book.notfound"));
    }

    @Transactional
    public BookDTO saveFromDTO(BookDTO dto) {
        var author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("error.author.notfound"));

        if (bookRepository.existsByTitleAndAuthorId(dto.getTitle(), dto.getAuthorId())) {
            throw new BookAlreadyExistsException("error.book.exists");
        }

        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setGenre(dto.getGenre());
        book.setPublishYear(dto.getPublishYear());
        book.setAuthor(author);

        return toDTO(bookRepository.save(book));
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO updatedBook) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("error.book.notfound"));

        book.setTitle(updatedBook.getTitle());
        book.setGenre(updatedBook.getGenre());
        book.setPublishYear(updatedBook.getPublishYear());

        var author = authorRepository.findById(updatedBook.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("error.author.notfound"));

        book.setAuthor(author);

        return toDTO(bookRepository.save(book));
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            bookRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new BookNotFoundException("error.book.notfound");
        }
    }

    // MAPOWANIE DO DTO

    private BookDTO toDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setGenre(book.getGenre());
        dto.setPublishYear(book.getPublishYear());
        dto.setAuthorId(book.getAuthor().getId());
        dto.setAuthorName(book.getAuthor().getName());
        dto.setAuthorSurname(book.getAuthor().getSurname());

        // UWAGA: potencjalny N+1 – świadomie zaakceptowane na tym etapie
        dto.setAverageRating(
                bookRepository.findAverageRatingForBook(book.getId())
        );

        return dto;
    }
}