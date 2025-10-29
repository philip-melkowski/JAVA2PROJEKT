package pl.melkowskiphilip.GoodReadsPL.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.melkowskiphilip.GoodReadsPL.dto.AuthorDTO;
import pl.melkowskiphilip.GoodReadsPL.service.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors()
    {
        return ResponseEntity.ok(authorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id)
    {
        AuthorDTO author = authorService.findById(id);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }


}
