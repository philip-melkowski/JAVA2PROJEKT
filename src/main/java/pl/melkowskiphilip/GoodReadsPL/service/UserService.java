package pl.melkowskiphilip.GoodReadsPL.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.melkowskiphilip.GoodReadsPL.dto.UserDTO;
import pl.melkowskiphilip.GoodReadsPL.dto.UserRegisterDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.Role;
import pl.melkowskiphilip.GoodReadsPL.entity.User;
import pl.melkowskiphilip.GoodReadsPL.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Pobranie wszystkich użytkowników
    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Pobranie użytkownika po ID
    public UserDTO findById(Long id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow( () ->
                        new IllegalArgumentException("Uzytkownik o ID " + id + " nie istnieje"));
    }


    // Pobranie użytkownika po loginie (bez względu na wielkość liter)
    public UserDTO findByUsernameIgnoreCase(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .map(this::toDTO)
                .orElseThrow( () ->
                        new IllegalArgumentException("Uzytkownik o loginie " + username + " nie istnieje case insensitive"));
    }

    // Pobranie użytkownika po e-mailu
    public UserDTO findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::toDTO)
                .orElseThrow( () ->
                        new IllegalArgumentException("Uzytkownik o adresie e-mail " + email + " nie istnieje"));
    }

    // Sprawdzenie, czy e-mail istnieje w bazie
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Sprawdzenie, czy login istnieje w bazie
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // niepotrzebne
    /*
    // Zapis nowego użytkownika (rejestracja)
    @Transactional
    public UserDTO save(UserRegisterDTO dto) {
        if (existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Adres e-mail jest już zajęty!");
        }
        if (existsByUsername(dto.getUsername())) {
            throw new IllegalStateException("Nazwa użytkownika jest już zajęta!");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(false);
        user.setRole(Role.USER);
        User saved = userRepository.save(user);
        return toDTO(saved);
    }
     */

    // Usunięcie użytkownika
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // aktualizacja użytkownika, np. po klikneiciu maila do aktywacji konta
    @Transactional
    public UserDTO updateUser(Long id, UserDTO updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik o ID " + id + " nie istnieje"));


        // Przykład: aktywacja konta po kliknięciu w link
        if (updatedUser.isEnabled() != existingUser.isEnabled()) {
            existingUser.setEnabled(updatedUser.isEnabled());
        }

        // zmiana maila
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
            if (existsByEmail(updatedUser.getEmail())) {
                throw new IllegalStateException("Ten adres e-mail jest już zajęty!");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        User saved = userRepository.save(existingUser);

        return toDTO(saved);
    }


    // Średnia liczba recenzji przypadająca na użytkownika
    public Double findAverageReviewCount() {
        return userRepository.findAverageReviewCount();
    }

    // Liczba recenzji danego użytkownika
    public Integer findReviewCount(Long id) {
        return userRepository.findReviewCount(id);
    }

    // User -> UserDTO
    public UserDTO toDTO(User user)
    {
        if(user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        return dto;
    }
}