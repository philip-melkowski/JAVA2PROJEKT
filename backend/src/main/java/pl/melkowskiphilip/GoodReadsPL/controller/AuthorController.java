package pl.melkowskiphilip.GoodReadsPL.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.melkowskiphilip.GoodReadsPL.dto.AuthorDTO;
import pl.melkowskiphilip.GoodReadsPL.service.AuthorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    // pobranie listy wszystkich autorow
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors()
    {
        return ResponseEntity.ok(authorService.findAll());
    }


    // pobranie autora po id
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id)
    {

        return ResponseEntity.ok(authorService.findById(id));
    }


        // dodanie autora
        @PostMapping
        public ResponseEntity<AuthorDTO> addAuthor(@Valid @RequestBody AuthorDTO authorDTO)
        {
            return ResponseEntity.ok(authorService.saveFromDTO(authorDTO));
        }


        // usuniecie autora po id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable Long id)
    {
        authorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // update autora
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDTO updatedAuthor)
    {
        return ResponseEntity.ok(authorService.updateAuthor(id, updatedAuthor));
    }

    // wypisanie autorow po fragmencie w imieniu lub nazwisku
    @GetMapping("/search")
    public ResponseEntity<List<AuthorDTO>> findByNameOrSurnameContainingCaseInsensitive(@RequestParam String fragment)
    {
        return ResponseEntity.ok(authorService.findByNameOrSurnameContainingCaseInsensitive(fragment));
    }

}
