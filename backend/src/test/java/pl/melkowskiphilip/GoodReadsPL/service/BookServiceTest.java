package pl.melkowskiphilip.GoodReadsPL.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.melkowskiphilip.GoodReadsPL.dto.BookDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.Author;
import pl.melkowskiphilip.GoodReadsPL.entity.Book;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.AuthorNotFoundException;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.BookAlreadyExistsException;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.BookNotFoundException;
import pl.melkowskiphilip.GoodReadsPL.repository.AuthorRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
       findAll()
       ========================= */

    @Test
    void shouldReturnAllBooks() {
        Book b1 = new Book();
        Book b2 = new Book();

        Author author = new Author();
        author.setId(1L);
        author.setName("Adam");
        author.setSurname("Mickiewicz");
        b1.setAuthor(author);
        b2.setAuthor(author);

        when(bookRepository.findAll()).thenReturn(List.of(b1, b2));
        when(bookRepository.findAverageRatingForBook(any())).thenReturn(4.0);

        List<BookDTO> result = bookService.findAll();

        assertEquals(2, result.size());
        verify(bookRepository).findAll();
    }

    /* =========================
       getAllBooksSorted()
       ========================= */

    @Test
    void shouldReturnBooksSortedAscending() {
        Book book = new Book();
        Author author = new Author();
        author.setId(1L);
        author.setName("A");
        author.setSurname("B");
        book.setAuthor(author);

        when(bookRepository.findAll(any(Sort.class))).thenReturn(List.of(book));
        when(bookRepository.findAverageRatingForBook(any())).thenReturn(5.0);

        List<BookDTO> result = bookService.getAllBooksSorted("title", "asc");

        assertEquals(1, result.size());
        verify(bookRepository).findAll(any(Sort.class));
    }

    /* =========================
       getPage()
       ========================= */

    @Test
    void shouldReturnPageOfBooks() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Adam");
        author.setSurname("Mickiewicz");

        Book book = new Book();
        book.setAuthor(author);

        Page<Book> page = new PageImpl<>(List.of(book));

        when(bookRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(bookRepository.findAverageRatingForBook(any())).thenReturn(4.5);

        Page<BookDTO> result = bookService.getPage(0, 5);

        assertEquals(1, result.getTotalElements());
    }

    /* =========================
       findById()
       ========================= */

    @Test
    void shouldReturnBookById() {
        Book book = new Book();
        book.setId(1L);

        Author author = new Author();
        author.setId(1L);
        author.setName("Adam");
        author.setSurname("Mickiewicz");
        book.setAuthor(author);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findAverageRatingForBook(1L)).thenReturn(4.0);

        BookDTO result = bookService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookService.findById(1L));
    }

    /* =========================
       saveFromDTO()
       ========================= */

    @Test
    void shouldSaveBookWhenNotExists() {
        BookDTO dto = new BookDTO();
        dto.setTitle("Pan Tadeusz");
        dto.setAuthorId(1L);

        Author author = new Author();
        author.setId(1L);
        author.setName("Adam");
        author.setSurname("Mickiewicz");

        Book saved = new Book();
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
    void shouldThrowExceptionWhenBookAlreadyExists() {
        BookDTO dto = new BookDTO();
        dto.setTitle("Pan Tadeusz");
        dto.setAuthorId(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author()));
        when(bookRepository.existsByTitleAndAuthorId("Pan Tadeusz", 1L))
                .thenReturn(true);

        assertThrows(BookAlreadyExistsException.class,
                () -> bookService.saveFromDTO(dto));

        verify(bookRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {
        BookDTO dto = new BookDTO();
        dto.setAuthorId(99L);

        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class,
                () -> bookService.saveFromDTO(dto));
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
    void shouldThrowExceptionWhenDeletingNonExistingBook() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(bookRepository).deleteById(1L);

        assertThrows(BookNotFoundException.class,
                () -> bookService.deleteById(1L));
    }

    /* =========================
       updateBook()
       ========================= */

    @Test
    void shouldUpdateBookWhenExists() {
        Book book = new Book();
        book.setId(1L);

        Author author = new Author();
        author.setId(1L);
        author.setName("Adam");
        author.setSurname("Mickiewicz");
        book.setAuthor(author);

        BookDTO updated = new BookDTO();
        updated.setTitle("Nowy tytuł");
        updated.setAuthorId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookRepository.findAverageRatingForBook(any())).thenReturn(4.0);

        BookDTO result = bookService.updateBook(1L, updated);

        assertEquals("Nowy tytuł", result.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookService.updateBook(1L, new BookDTO()));
    }

    /* =========================
       sortBooksByAverageRating
       ========================= */

    @Test
    void shouldSortBooksDescending() {
        BookDTO b1 = new BookDTO();
        b1.setAverageRating(5.0);

        BookDTO b2 = new BookDTO();
        b2.setAverageRating(2.0);

        List<BookDTO> result =
                bookService.sortBooksByAverageRatingDesc(List.of(b2, b1));

        assertEquals(5.0, result.get(0).getAverageRating());
    }
}