package pl.melkowskiphilip.GoodReadsPL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.melkowskiphilip.GoodReadsPL.entity.Book;
import pl.melkowskiphilip.GoodReadsPL.entity.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    // Znajdź książki po tytule (dokładna nazwa)
    Optional<Book> findByTitleIgnoreCase(String title);

    // Znajdź książki zawierające fragment tytułu
    List<Book> findByTitleContainingIgnoreCase(String part);

    // Znajdź książki danego autora
    List<Book> findAllByAuthorId(Long authorId);

    // Znajdź książki danego gatunku
    List<Book> findAllByGenre(Genre genre);

    // Sprawdź, czy książka już istnieje (np. przy dodawaniu)
    boolean existsByTitleAndAuthorId(String title, Long authorId);

    //   średnia ocen
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double findAverageRatingForBook(Long bookId);


    // książki z najwyższą średnią oceną
    @Query("""
           SELECT b FROM Book b 
           JOIN b.reviews r 
           GROUP BY b 
           ORDER BY AVG(r.rating) DESC
           """)
    List<Book> findTopRatedBooks();

    // książki z najniższą średnią oceną
    @Query("SELECT b from Book b JOIN b.reviews r GROUP BY b ORDER BY AVG(r.rating) ASC")
    List<Book> findBottomRatedBooks();

    // zwraca ilość wystawionych poszczególnych ocen dla książki
    @Query("""
       SELECT r.rating, COUNT(r)
       FROM Review r
       WHERE r.book.id = :bookId
       GROUP BY r.rating
       ORDER BY r.rating ASC
       """)
    List<Object[]> findRatingDistributionForBook(Long bookId);


    // książki powyżej danej średniej ocen
    @Query("""
       SELECT b
       FROM Book b
       JOIN b.reviews r
       GROUP BY b
       HAVING AVG(r.rating) > :rating
       """)
    List<Book> findAllWithAverageRatingGreaterThan(double rating);

    // książki poniżej danej średniej ocen
    @Query("""
       SELECT b
       FROM Book b
       JOIN b.reviews r
       GROUP BY b
       HAVING AVG(r.rating) < :rating
       """)
    List<Book> findAllWithAverageRatingLessThan(double rating);

    // książki z danego gatunku powyżej danej średniej
    @Query("""
       SELECT b
       FROM Book b
       JOIN b.reviews r
       WHERE b.genre = :genre
       GROUP BY b
       HAVING AVG(r.rating) > :rating
       """)
    List<Book> findAllByGenreWithAverageRatingGreaterThan(Genre genre, double rating);

    // książki z danego gatunki poniżej danej średniej
    @Query("""
       SELECT b
       FROM Book b
       JOIN b.reviews r
       WHERE b.genre = :genre
       GROUP BY b
       HAVING AVG(r.rating) < :rating
       """)
    List<Book> findAllByGenreWithAverageRatingLessThan(Genre genre, double rating);

    // książki danego autora powyżej danej średniej
    @Query("""
       SELECT b
       FROM Book b
       JOIN b.reviews r
       WHERE b.author.id = :authorId
       GROUP BY b
       HAVING AVG(r.rating) > :rating
       ORDER BY AVG(r.rating) DESC
       """)
    List<Book> findBooksByAuthorWithAvgRatingGreaterThan(Long authorId, double rating);

    // książki danego autora poniżej danej średniej
    @Query("""
       SELECT b
       FROM Book b
       JOIN b.reviews r
       WHERE b.author.id = :authorId
       GROUP BY b
       HAVING AVG(r.rating) < :rating
       ORDER BY AVG(r.rating) DESC
       """)
    List<Book> findBooksByAuthorWithAvgRatingLessThan(Long authorId, double rating);
}




