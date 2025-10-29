package pl.melkowskiphilip.GoodReadsPL.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.melkowskiphilip.GoodReadsPL.entity.Review;
import pl.melkowskiphilip.GoodReadsPL.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 🔹 Wszystkie recenzje
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    // 🔹 Recenzje konkretnej książki
    public List<Review> findAllByBook(Long bookId) {
        return reviewRepository.findAllByBookId(bookId);
    }

    // 🔹 Recenzje konkretnego użytkownika
    public List<Review> findAllByUser(Long userId) {
        return reviewRepository.findAllByUserId(userId);
    }

    // 🔹 Jedna recenzja konkretnego użytkownika dla konkretnej książki
    public Optional<Review> findByBookAndUser(Long bookId, Long userId) {
        return reviewRepository.findByBookIdAndUserId(bookId, userId);
    }

    // 🔹 Sprawdzenie, czy użytkownik już ocenił książkę
    public boolean existsByBookAndUser(Long bookId, Long userId) {
        return reviewRepository.existsByBookIdAndUserId(bookId, userId);
    }

    // 🔹 Dodanie nowej recenzji (z kontrolą duplikatów)
    @Transactional
    public Review addReview(Review review) {
        Long bookId = review.getBook().getId();
        Long userId = review.getUser().getId();

        if (existsByBookAndUser(bookId, userId)) {
            throw new IllegalStateException("Użytkownik już ocenił tę książkę!");
        }

        return reviewRepository.save(review);
    }

    // 🔹 Aktualizacja istniejącej recenzji
    @Transactional
    public Review updateReview(Review review) {
        if (review.getId() == null) {
            throw new IllegalArgumentException("ID recenzji nie może być null przy aktualizacji");
        }
        return reviewRepository.save(review);
    }

    // 🔹 Usunięcie recenzji
    @Transactional
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    // 🔹 Recenzje z komentarzem dla danej książki
    public List<Review> findReviewsWithComments(Long bookId) {
        return reviewRepository.findAllByBookIdAndCommentIsNotNull(bookId);
    }

    // 🔹 Recenzje z komentarzem i daną oceną dla danej książki
    public List<Review> findReviewsWithCommentsByRating(Long bookId, int rating) {
        return reviewRepository.findAllByBookIdAndRatingAndCommentIsNotNull(bookId, rating);
    }
}