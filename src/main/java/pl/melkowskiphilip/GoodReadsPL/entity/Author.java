package pl.melkowskiphilip.GoodReadsPL.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;
    @Column(nullable = false, length = 50)
    private String surname;

    // cascadeType.ALL -    Jeśli coś robię na encji rodzica, to wykonaj to samo na dzieciach
    /*
    Jeśli zapiszesz autora, zapiszą się też jego książki.
    Jeśli zaktualizujesz autora, zaktualizują się też jego książki.
    Jeśli usuniesz autora, usuną się również jego książki.
     */

    // orphan removal - Jeśli dziecko zostanie usunięte z kolekcji w pamięci, usuń je też z bazy danych
    // przyklad:
    /*
    Author a = authorRepository.findById(1L).get();
    a.getBooks().remove(0);
    authorRepository.save(a);
     */
    // hibernate usunie ksiazke tez w bazie bo jest "osierocona" - nie ma powiazania z zadnym autorem
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();



}
