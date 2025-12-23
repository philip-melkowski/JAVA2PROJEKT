package pl.melkowskiphilip.GoodReadsPL.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.melkowskiphilip.GoodReadsPL.dto.AuthorDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.Author;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.AuthorAlreadyExistsException;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.AuthorNotFoundException;
import pl.melkowskiphilip.GoodReadsPL.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;

    //  Pobranie wszystkich autorów
    public List<AuthorDTO> findAll() {
        return authorRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    //  Pobranie autora po ID
    public AuthorDTO findById(Long id) {
        return authorRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new AuthorNotFoundException("Nie znaleziono autora o ID " + id));
    }

    // pobranie autorow po fragmencie w imieniu lub nazwisku
    public List<AuthorDTO> findByNameOrSurnameContainingCaseInsensitive(String fragment) {
        return authorRepository.findByNameOrSurnameContainingIgnoreCase(fragment)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    // Dodanie nowego autora (jeśli nie istnieje)
    @Transactional
    public AuthorDTO saveFromDTO(AuthorDTO dto) {
        boolean exists = authorRepository.existsByNameAndSurname(dto.getName(), dto.getSurname());
        if(exists)
        {
            throw new AuthorAlreadyExistsException("Autor juz istnieje w bazie");
        }
        Author author = new Author();
        author.setName(dto.getName());
        author.setSurname(dto.getSurname());
        Author saved = authorRepository.save(author);
        return toDTO(saved);
    }

    //  Usunięcie autora
    @Transactional
    public void deleteById(Long id) {
        try {
            authorRepository.deleteById(id);
        }
        catch(EmptyResultDataAccessException e)
        {
            throw new AuthorNotFoundException("Nie znaleziono autora o ID " + id);
        }
    }


    // aktualizacja danych autora
    @Transactional
    public AuthorDTO updateAuthor(Long id, AuthorDTO updatedAuthor)
    {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Nie znaleziono autora o ID " + id));
        author.setName(updatedAuthor.getName());
        author.setSurname(updatedAuthor.getSurname());
        return toDTO(authorRepository.save(author));
    }



    public AuthorDTO toDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setSurname(author.getSurname());

        return dto;
    }


}