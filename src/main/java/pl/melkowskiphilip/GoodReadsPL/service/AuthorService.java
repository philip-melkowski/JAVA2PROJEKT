package pl.melkowskiphilip.GoodReadsPL.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.melkowskiphilip.GoodReadsPL.dto.AuthorDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.Author;
import pl.melkowskiphilip.GoodReadsPL.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;

    // 🔹 Pobranie wszystkich autorów
    public List<AuthorDTO> findAll() {
        return authorRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Pobranie autora po ID
    public AuthorDTO findById(Long id) {
        return authorRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }


    // Sprawdzenie, czy autor już istnieje
    public boolean existsByNameAndSurname(String name, String surname) {
        return authorRepository.existsByNameAndSurname(name, surname);
    }

    // Dodanie nowego autora (jeśli nie istnieje)
    @Transactional
    public AuthorDTO saveFromDTO(AuthorDTO dto) {
        Author author = new Author();
        author.setName(dto.getName());
        author.setSurname(dto.getSurname());
        Author saved = authorRepository.save(author);
        return toDTO(saved);
    }

    // 🔹 Usunięcie autora
    @Transactional
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    // 🔹 Lista autorów z liczbą książek
    public List<Object[]> findAuthorsWithBookCount() {
        return authorRepository.findAuthorsWithBookCount();
    }

    // 🔹 Najpopularniejsi autorzy (po liczbie książek)
    public List<AuthorDTO> findTopAuthorsByBookCount() {
        return authorRepository.findTopAuthorsByBookCount()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO toDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setSurname(author.getSurname());

        return dto;
    }
}