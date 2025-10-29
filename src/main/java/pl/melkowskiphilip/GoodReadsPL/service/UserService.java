package pl.melkowskiphilip.GoodReadsPL.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.melkowskiphilip.GoodReadsPL.entity.Role;
import pl.melkowskiphilip.GoodReadsPL.entity.User;
import pl.melkowskiphilip.GoodReadsPL.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    // 🔹 Pobranie wszystkich użytkowników
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // 🔹 Pobranie użytkownika po ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // 🔹 Pobranie użytkownika po loginie
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 🔹 Pobranie użytkownika po loginie (bez względu na wielkość liter)
    public Optional<User> findByUsernameIgnoreCase(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    // 🔹 Pobranie użytkownika po e-mailu
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 🔹 Sprawdzenie, czy e-mail istnieje w bazie
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // 🔹 Sprawdzenie, czy login istnieje w bazie
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // 🔹 Zapis nowego użytkownika
    @Transactional
    public User save(User user) {
        if (existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Adres e-mail jest już zajęty!");
        }
        if (existsByUsername(user.getUsername())) {
            throw new IllegalStateException("Nazwa użytkownika jest już zajęta!");
        }
        return userRepository.save(user);
    }

    // 🔹 Usunięcie użytkownika
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // aktualizacja użytkownika, np. po klikneiciu maila do aktywacji konta
    @Transactional
    public User updateUser(Long id, User updatedUser) {
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

        // zmiana hasla
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword());
        }

        return userRepository.save(existingUser);
    }

    // 🔹 Wyszukanie użytkowników po roli (USER / ADMIN)
    public List<User> findAllByRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    // 🔹 Średnia liczba recenzji przypadająca na użytkownika
    public Double findAverageReviewCount() {
        return userRepository.findAverageReviewCount();
    }

    // 🔹 Liczba recenzji danego użytkownika
    public Integer findReviewCount(Long id) {
        return userRepository.findReviewCount(id);
    }
}