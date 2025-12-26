package pl.melkowskiphilip.GoodReadsPL.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.melkowskiphilip.GoodReadsPL.dto.BookDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.Author;
import pl.melkowskiphilip.GoodReadsPL.entity.Book;
import pl.melkowskiphilip.GoodReadsPL.entity.Genre;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.AuthorNotFoundException;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.BookAlreadyExistsException;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.BookNotFoundException;
import pl.melkowskiphilip.GoodReadsPL.repository.AuthorRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    /* =========================
       searchBooks()
       ========================= */

    @Test
    void shouldReturnPageOfBooksWithSearch() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Adam");
        author.setSurname("Mickiewicz");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Pan Tadeusz");
        book.setGenre(Genre.FANTASY);
        book.setAuthor(author);

        Page<Book> page = new PageImpl<>(List.of(book));

        when(bookRepository.findAll(
                ArgumentMatchers.<org.springframework.data.jpa.domain.Specification<Book>>any(),
                any(Pageable.class)
        )).thenReturn(page);
        when(bookRepository.findAverageRatingForBook(1L)).thenReturn(4.5);

        Page<BookDTO> result = bookService.searchBooks(
                0,
                10,
                "title",
                "asc",
                Genre.FANTASY,
                1L,
                "Pan"
        );

        assertEquals(1, result.getTotalElements());
        assertEquals("Pan Tadeusz", result.getContent().get(0).getTitle());


    }

    /* =========================
       findById()
       ========================= */

    @Test
    void shouldReturnBookById() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Adam");
        author.setSurname("Mickiewicz");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Pan Tadeusz");
        book.setAuthor(author);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findAverageRatingForBook(1L)).thenReturn(4.0);

        BookDTO result = bookService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Pan Tadeusz", result.getTitle());
    }

    @Test
    void shouldThrowWhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookService.findById(1L));
    }

    /* =========================
       saveFromDTO()
       ========================= */

    @Test
    void shouldSaveBookWhenNotExists() {
        Author author = new Author();
        author.setId(1L);

        BookDTO dto = new BookDTO();
        dto.setTitle("Pan Tadeusz");
        dto.setAuthorId(1L);
        dto.setGenre(Genre.FANTASY);

        Book saved = new Book();
        saved.setId(1L);
        saved.setAuthor(author);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.existsByTitleAndAuthorId("Pan Tadeusz", 1L))
                .thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(saved);
        when(bookRepository.findAverageRatingForBook(any())).thenReturn(0.0);

        BookDTO result = bookService.saveFromDTO(dto);

        assertNotNull(result);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void shouldThrowWhenBookAlreadyExists() {
        BookDTO dto = new BookDTO();
        dto.setTitle("Pan Tadeusz");
        dto.setAuthorId(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author()));
        when(bookRepository.existsByTitleAndAuthorId("Pan Tadeusz", 1L))
                .thenReturn(true);

        assertThrows(BookAlreadyExistsException.class,
                () -> bookService.saveFromDTO(dto));
    }

    @Test
    void shouldThrowWhenAuthorNotFound() {
        BookDTO dto = new BookDTO();
        dto.setAuthorId(99L);

        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class,
                () -> bookService.saveFromDTO(dto));
    }

    /* =========================
       updateBook()
       ========================= */

    @Test
    void shouldUpdateBookWhenExists() {
        Author author = new Author();
        author.setId(1L);

        Book book = new Book();
        book.setId(1L);
        book.setAuthor(author);

        BookDTO updated = new BookDTO();
        updated.setTitle("Nowy tytuł");
        updated.setAuthorId(1L);
        updated.setGenre(Genre.HISTORY);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookRepository.findAverageRatingForBook(any())).thenReturn(4.0);

        BookDTO result = bookService.updateBook(1L, updated);

        assertEquals("Nowy tytuł", result.getTitle());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookService.updateBook(1L, new BookDTO()));
    }

    /* =========================
       deleteById()
       ========================= */

    @Test
    void shouldDeleteBookWhenExists() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteById(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingBook() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(bookRepository).deleteById(1L);

        assertThrows(BookNotFoundException.class,
                () -> bookService.deleteById(1L));
    }
}