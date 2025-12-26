package pl.melkowskiphilip.GoodReadsPL.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import pl.melkowskiphilip.GoodReadsPL.dto.UserDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.User;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.EmailAlreadyUsedException;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.UserNotFoundException;
import pl.melkowskiphilip.GoodReadsPL.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private UserService userService;

    /* =========================
       findAll()
       ========================= */

    @Test
    void shouldReturnAllUsers() {
        User u1 = new User();
        User u2 = new User();

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UserDTO> result = userService.findAll();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    /* =========================
       findById()
       ========================= */

    @Test
    void shouldReturnUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("user@test.pl");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.findById(1L);

        assertEquals("user", result.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findById(1L));
    }

    /* =========================
       findByUsernameIgnoreCase()
       ========================= */

    @Test
    void shouldReturnUserByUsernameIgnoreCase() {
        User user = new User();
        user.setUsername("User");

        when(userRepository.findByUsernameIgnoreCase("user"))
                .thenReturn(Optional.of(user));

        UserDTO result = userService.findByUsernameIgnoreCase("user");

        assertEquals("User", result.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUsernameNotFound() {
        when(userRepository.findByUsernameIgnoreCase("user"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findByUsernameIgnoreCase("user"));
    }

    /* =========================
       findByEmail()
       ========================= */

    @Test
    void shouldReturnUserByEmail() {
        User user = new User();
        user.setEmail("test@test.pl");

        when(userRepository.findByEmail("test@test.pl"))
                .thenReturn(Optional.of(user));

        UserDTO result = userService.findByEmail("test@test.pl");

        assertEquals("test@test.pl", result.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenEmailNotFound() {
        when(userRepository.findByEmail("test@test.pl"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findByEmail("test@test.pl"));
    }

    /* =========================
       existsByEmail / existsByUsername
       ========================= */

    @Test
    void shouldReturnTrueWhenEmailExists() {
        when(userRepository.existsByEmail("test@test.pl")).thenReturn(true);

        assertTrue(userService.existsByEmail("test@test.pl"));
    }

    @Test
    void shouldReturnTrueWhenUsernameExists() {
        when(userRepository.existsByUsername("user")).thenReturn(true);

        assertTrue(userService.existsByUsername("user"));
    }

    /* =========================
       deleteById()
       ========================= */

    @Test
    void shouldDeleteUserWhenExists() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(userRepository).deleteById(1L);

        assertThrows(UserNotFoundException.class,
                () -> userService.deleteById(1L));
    }

    /* =========================
       updateUser()
       ========================= */

    @Test
    void shouldUpdateUserWhenEmailChanged() {
        User existing = new User();
        existing.setId(1L);
        existing.setEmail("old@test.pl");
        existing.setEnabled(false);

        UserDTO updated = new UserDTO();
        updated.setEmail("new@test.pl");
        updated.setEnabled(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmail("new@test.pl")).thenReturn(false);
        when(userRepository.save(existing)).thenReturn(existing);

        UserDTO result = userService.updateUser(1L, updated);

        assertTrue(result.isEnabled());
        assertEquals("new@test.pl", result.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyUsed() {
        User existing = new User();
        existing.setId(1L);
        existing.setEmail("old@test.pl");

        UserDTO updated = new UserDTO();
        updated.setEmail("taken@test.pl");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmail("taken@test.pl")).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class,
                () -> userService.updateUser(1L, updated));

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(1L, new UserDTO()));
    }

    /* =========================
       stats
       ========================= */

    @Test
    void shouldReturnAverageReviewCount() {
        when(userRepository.findAverageReviewCount()).thenReturn(2.5);

        Double result = userService.findAverageReviewCount();

        assertEquals(2.5, result);
    }

    @Test
    void shouldReturnReviewCountForUser() {
        when(userRepository.findReviewCount(1L)).thenReturn(3);

        Integer result = userService.findReviewCount(1L);

        assertEquals(3, result);
    }
}