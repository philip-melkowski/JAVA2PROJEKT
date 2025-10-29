package pl.melkowskiphilip.GoodReadsPL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.melkowskiphilip.GoodReadsPL.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByBookId(Long bookId);

    List<Review> findAllByUserId(Long id);

    boolean existsByBookIdAndUserId(Long bookId, Long userId);

    Optional<Review> findByBookIdAndUserId(Long bookId, Long userId);

    // niepotrzebne, już jest w BookRepo
    /*
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double findAverageRatingForBook(Long bookId);
     */
    // Zwraca wszystkie recenzje, gdzie book.id = :bookId i comment IS NOT NULL
    List<Review> findAllByBookIdAndCommentIsNotNull(Long bookId);

    // Zwraca wszystkie recenzje książki o określonej ocenie (rating = x), które mają komentarz
    List<Review> findAllByBookIdAndRatingAndCommentIsNotNull(Long bookId, int rating);
}
