package pl.melkowskiphilip.GoodReadsPL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.melkowskiphilip.GoodReadsPL.entity.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByNameAndSurname(String name, String surname);
    List<Author> findAllBySurname(String surname);
    List<Author> findBySurnameContainingIgnoreCase(String part);
    List<Author> findByNameContainingIgnoreCase(String part);
    boolean existsByNameAndSurname(String name, String surname);

    // zwraca np.: [J.K. Rowling, 7]
    @Query("SELECT a, size(a.books) FROM Author a")
    List<Object[]> findAuthorsWithBookCount();

    @Query("SELECT a FROM Author a LEFT JOIN a.books b GROUP BY a ORDER BY COUNT(b) DESC")
    List<Author> findTopAuthorsByBookCount();
}
