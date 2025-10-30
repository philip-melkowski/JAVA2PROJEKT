package pl.melkowskiphilip.GoodReadsPL.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.melkowskiphilip.GoodReadsPL.dto.ReviewDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.Book;
import pl.melkowskiphilip.GoodReadsPL.entity.Review;
import pl.melkowskiphilip.GoodReadsPL.entity.User;
import pl.melkowskiphilip.GoodReadsPL.repository.BookRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.ReviewRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    // 🔹 Wszystkie recenzje
    public List<ReviewDTO> findAll() {
        return reviewRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Recenzje konkretnej książki
    public List<ReviewDTO> findAllByBook(Long bookId) {
        return reviewRepository.findAllByBookId(bookId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Recenzje konkretnego użytkownika
    public List<ReviewDTO> findAllByUser(Long userId) {
        return reviewRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Jedna recenzja konkretnego użytkownika dla konkretnej książki
    public ReviewDTO findByBookAndUser(Long bookId, Long userId) {
        Optional<Review> rev = reviewRepository.findByBookIdAndUserId(bookId, userId);
        return rev.map(this::toDTO).orElse(null);
    }

    // 🔹 Sprawdzenie, czy użytkownik już ocenił książkę
    public boolean existsByBookAndUser(Long bookId, Long userId) {
        return reviewRepository.existsByBookIdAndUserId(bookId, userId);
    }

    // 🔹 Dodanie nowej recenzji (z kontrolą duplikatów)
    @Transactional
    public ReviewDTO saveReview(ReviewDTO dto) {

        // 1️⃣ Pobranie powiązanej książki i użytkownika
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono książki o ID " + dto.getBookId()));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika o ID " + dto.getUserId()));

        // 2️⃣ Sprawdzenie, czy użytkownik już oceniał tę książkę
        if (reviewRepository.existsByBookIdAndUserId(dto.getBookId(), dto.getUserId())) {
            throw new IllegalArgumentException("Użytkownik już dodał recenzję dla tej książki");
        }

        // 3️⃣ Utworzenie nowej recenzji
        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setBook(book);
        review.setUser(user);

        // createdAt ustawi się automatycznie w encji (masz domyślnie = LocalDateTime.now())

        // 4️⃣ Zapis w bazie
        Review saved = reviewRepository.save(review);

        // 5️⃣ Zwrócenie DTO (mapowanie encji na DTO)
        return toDTO(saved);
    }

    // 🔹 Aktualizacja istniejącej recenzji
    @Transactional
    public ReviewDTO updateReview(ReviewDTO review) {
        if (review.getId() == null) {
            throw new IllegalArgumentException("ID recenzji nie może być null przy aktualizacji");
        }
        Review existingReview = reviewRepository.findById(review.getId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono recenzji o ID " + review.getId()));
        existingReview.setRating(review.getRating());
        existingReview.setComment(review.getComment());

        return toDTO(reviewRepository.save(existingReview));
    }

    // 🔹 Usunięcie recenzji
    @Transactional
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    // 🔹 Recenzje z komentarzem dla danej książki
    public List<ReviewDTO> findReviewsWithComments(Long bookId) {
        return reviewRepository.findAllByBookIdAndCommentIsNotNull(bookId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 Recenzje z komentarzem i daną oceną dla danej książki
    public List<ReviewDTO> findReviewsWithCommentsByRating(Long bookId, int rating) {
        return reviewRepository.findAllByBookIdAndRatingAndCommentIsNotNull(bookId, rating)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO toDTO(Review review)
    {
        if(review == null) return null;
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setBookTitle(review.getBook().getTitle());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setBookId(review.getBook().getId());
        dto.setUserId(review.getUser().getId());
        return dto;
    }
}