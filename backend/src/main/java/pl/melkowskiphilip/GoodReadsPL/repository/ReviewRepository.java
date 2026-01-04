package pl.melkowskiphilip.GoodReadsPL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.melkowskiphilip.GoodReadsPL.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByBookId(Long bookId);

    @Query("""
    select r from Review r
    join fetch r.book b
    join fetch b.author
    where r.user.id = :userId
    """)
    List<Review> findAllByUserId(Long id);

    @Query("""
    select r.book.id from Review r
        where r.user.id = :userId
    """)
    List<Long> findReviewedBooksIdsByUserId(@Param("userId") Long userId);

    boolean existsByBookIdAndUserId(Long bookId, Long userId);

    Optional<Review> findByBookIdAndUserId(Long bookId, Long userId);

    // Zwraca wszystkie recenzje, gdzie book.id = :bookId i comment IS NOT NULL
    List<Review> findAllByBookIdAndCommentIsNotNull(Long bookId);

    // Zwraca wszystkie recenzje książki o określonej ocenie (rating = x), które mają komentarz
    List<Review> findAllByBookIdAndRatingAndCommentIsNotNull(Long bookId, int rating);
}
