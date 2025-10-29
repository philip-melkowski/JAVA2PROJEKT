package pl.melkowskiphilip.GoodReadsPL.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.melkowskiphilip.GoodReadsPL.entity.Author;
import pl.melkowskiphilip.GoodReadsPL.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;

    // 🔹 Pobranie wszystkich autorów
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    // 🔹 Pobranie autora po ID
    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    // 🔹 Wyszukanie autora po imieniu i nazwisku
    public Optional<Author> findByNameAndSurname(String name, String surname) {
        return authorRepository.findByNameAndSurname(name, surname);
    }

    // 🔹 Wyszukiwanie po fragmencie nazwiska (np. "row" → "Rowling")
    public List<Author> searchBySurnamePart(String part) {
        return authorRepository.findBySurnameContainingIgnoreCase(part);
    }

    // 🔹 Wyszukiwanie po fragmencie imienia
    public List<Author> searchByNamePart(String part) {
        return authorRepository.findByNameContainingIgnoreCase(part);
    }

    // 🔹 Sprawdzenie, czy autor już istnieje
    public boolean existsByNameAndSurname(String name, String surname) {
        return authorRepository.existsByNameAndSurname(name, surname);
    }

    // 🔹 Dodanie nowego autora (jeśli nie istnieje)
    @Transactional
    public Author addAuthor(Author author) {
        if (authorRepository.existsByNameAndSurname(author.getName(), author.getSurname())) {
            throw new IllegalStateException("Autor o tym imieniu i nazwisku już istnieje");
        }
        return authorRepository.save(author);
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
    public List<Author> findTopAuthorsByBookCount() {
        return authorRepository.findTopAuthorsByBookCount();
    }
}