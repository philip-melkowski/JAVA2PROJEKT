package pl.melkowskiphilip.GoodReadsPL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.melkowskiphilip.GoodReadsPL.entity.Book;

// JpaSpecificationExecutor pozwala budować dynamiczne zapytania do bazy danych w czasie działania aplikacji (runtime)
// – zamiast mieć dziesiątki metod findByXAndYAndZ.
@Repository
public interface BookRepository
        extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {

    // średnia ocen książki
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double findAverageRatingForBook(Long bookId);

    // sprawdza czy istnieje już książka o tym samym tytule i autorze
    boolean existsByTitleAndAuthorId(String title, Long authorId);
}