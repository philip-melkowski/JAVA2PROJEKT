package pl.melkowskiphilip.GoodReadsPL.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.melkowskiphilip.GoodReadsPL.dto.AuthorDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.Author;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.AuthorAlreadyExistsException;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.AuthorNotFoundException;
import pl.melkowskiphilip.GoodReadsPL.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    /* =========================
       findAll()
       ========================= */

    @Test
    void shouldReturnAllAuthors() {
        Author a1 = new Author();
        Author a2 = new Author();
        Author a3 = new Author();

        when(authorRepository.findAll()).thenReturn(List.of(a1, a2, a3));

        List<AuthorDTO> result = authorService.findAll();

        assertEquals(3, result.size());
        verify(authorRepository, times(1)).findAll();
    }

    /* =========================
       findById()
       ========================= */

    @Test
    void shouldReturnAuthorById() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Adam");
        author.setSurname("Mickiewicz");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorDTO result = authorService.findById(1L);

        assertEquals("Adam", result.getName());
        assertEquals("Mickiewicz", result.getSurname());
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class,
                () -> authorService.findById(1L));
    }

    /* =========================
       findByNameOrSurnameContainingCaseInsensitive()
       ========================= */

    @Test
    void shouldFindAuthorsByNameOrSurnameFragment() {
        Author author = new Author();
        author.setName("Henryk");
        author.setSurname("Sienkiewicz");

        when(authorRepository.findByNameOrSurnameContainingIgnoreCase("hen"))
                .thenReturn(List.of(author));

        List<AuthorDTO> result =
                authorService.findByNameOrSurnameContainingCaseInsensitive("hen");

        assertEquals(1, result.size());
        assertEquals("Henryk", result.getFirst().getName());
    }

    /* =========================
       saveFromDTO()
       ========================= */

    @Test
    void shouldSaveAuthorWhenNotExists() {
        AuthorDTO dto = new AuthorDTO();
        dto.setName("Bolesław");
        dto.setSurname("Prus");

        Author savedAuthor = new Author();
        savedAuthor.setName("Bolesław");
        savedAuthor.setSurname("Prus");

        when(authorRepository.existsByNameAndSurname("Bolesław", "Prus"))
                .thenReturn(false);
        when(authorRepository.save(any(Author.class)))
                .thenReturn(savedAuthor);

        AuthorDTO result = authorService.saveFromDTO(dto);

        assertEquals("Bolesław", result.getName());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void shouldThrowExceptionWhenAuthorAlreadyExists() {
        AuthorDTO dto = new AuthorDTO();
        dto.setName("Adam");
        dto.setSurname("Mickiewicz");

        when(authorRepository.existsByNameAndSurname("Adam", "Mickiewicz"))
                .thenReturn(true);

        assertThrows(AuthorAlreadyExistsException.class,
                () -> authorService.saveFromDTO(dto));

        verify(authorRepository, never()).save(any());
    }

    /* =========================
       deleteById()
       ========================= */

    @Test
    void shouldDeleteAuthorWhenExists() {
        doNothing().when(authorRepository).deleteById(1L);

        authorService.deleteById(1L);

        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingAuthor() {
        doThrow(new org.springframework.dao.EmptyResultDataAccessException(1))
                .when(authorRepository).deleteById(1L);

        assertThrows(AuthorNotFoundException.class,
                () -> authorService.deleteById(1L));
    }

    /* =========================
       updateAuthor()
       ========================= */

    @Test
    void shouldUpdateAuthorWhenExists() {
        Author existing = new Author();
        existing.setId(1L);
        existing.setName("Old");
        existing.setSurname("Name");

        AuthorDTO updated = new AuthorDTO();
        updated.setName("New");
        updated.setSurname("Name");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(authorRepository.save(any(Author.class))).thenReturn(existing);

        AuthorDTO result = authorService.updateAuthor(1L, updated);

        assertEquals("New", result.getName());
        verify(authorRepository, times(1)).save(existing);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingAuthor() {
        AuthorDTO updated = new AuthorDTO();
        updated.setName("New");
        updated.setSurname("Name");

        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class,
                () -> authorService.updateAuthor(1L, updated));
    }
}