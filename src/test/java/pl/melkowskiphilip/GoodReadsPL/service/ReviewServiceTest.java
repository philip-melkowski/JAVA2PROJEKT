package pl.melkowskiphilip.GoodReadsPL.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.melkowskiphilip.GoodReadsPL.dto.ReviewDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.Book;
import pl.melkowskiphilip.GoodReadsPL.entity.Review;
import pl.melkowskiphilip.GoodReadsPL.entity.User;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.*;
import pl.melkowskiphilip.GoodReadsPL.repository.BookRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.ReviewRepository;
import pl.melkowskiphilip.GoodReadsPL.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewService reviewService;

    /* =========================
       findAll()
       ========================= */

    @Test
    void shouldReturnAllReviews() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book");

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(5);


        when(reviewRepository.findAll()).thenReturn(List.of(review));

        List<ReviewDTO> result = reviewService.findAll();

        assertEquals(1, result.size());
    }

    /* =========================
       findByBookAndUser()
       ========================= */

    @Test
    void shouldReturnReviewByBookAndUser() {
        Review review = new Review();
        User user = new User();
        user.setUsername("user");
        user.setId(1L);

        Book book = new Book();
        book.setTitle("Book");
        book.setId(1L);

        review.setUser(user);
        review.setBook(book);
        review.setRating(5);

        when(reviewRepository.findByBookIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(review));

        ReviewDTO dto = reviewService.findByBookAndUser(1L, 1L);

        assertEquals(5, dto.getRating());
    }

    @Test
    void shouldThrowExceptionWhenReviewNotFound() {
        when(reviewRepository.findByBookIdAndUserId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class,
                () -> reviewService.findByBookAndUser(1L, 1L));
    }

    /* =========================
       saveReview()
       ========================= */

    @Test
    void shouldSaveReviewWhenNotExists() {
        ReviewDTO dto = new ReviewDTO();
        dto.setBookId(1L);
        dto.setUserId(1L);
        dto.setRating(4);
        dto.setComment("OK");

        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setReadBooks(new java.util.HashSet<>());

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book");
        book.setReaders(new java.util.HashSet<>());
        book.setReviews(new java.util.ArrayList<>());

        Review saved = new Review();
        saved.setUser(user);
        saved.setBook(book);
        saved.setRating(4);
        saved.setComment("OK");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepository.existsByBookIdAndUserId(1L, 1L)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(saved);

        ReviewDTO result = reviewService.saveReview(dto);

        assertEquals(4, result.getRating());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenReviewAlreadyExists() {
        ReviewDTO dto = new ReviewDTO();
        dto.setBookId(1L);
        dto.setUserId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(reviewRepository.existsByBookIdAndUserId(1L, 1L)).thenReturn(true);

        assertThrows(ReviewAlreadyExistsException.class,
                () -> reviewService.saveReview(dto));

        verify(reviewRepository, never()).save(any());
    }

    /* =========================
       updateReview()
       ========================= */

    @Test
    void shouldUpdateReviewWhenExists() {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(1L);
        dto.setRating(5);
        dto.setComment("Updated");

        Review existing = new Review();
        existing.setId(1L);

        User user = new User();
        user.setUsername("user");
        user.setId(1L);

        Book book = new Book();
        book.setTitle("Book");
        book.setId(1L);

        existing.setUser(user);
        existing.setBook(book);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(reviewRepository.save(any(Review.class))).thenReturn(existing);

        ReviewDTO result = reviewService.updateReview(dto);

        assertEquals(5, result.getRating());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithNullId() {
        ReviewDTO dto = new ReviewDTO();

        assertThrows(InvalidReviewIdException.class,
                () -> reviewService.updateReview(dto));
    }

    /* =========================
       deleteById()
       ========================= */

    @Test
    void shouldDeleteReviewAndUpdateRelations() {
        User user = new User();
        user.setId(1L);
        user.setReadBooks(new java.util.HashSet<>());

        Book book = new Book();
        book.setId(1L);
        book.setReaders(new java.util.HashSet<>());
        book.setReviews(new java.util.ArrayList<>());

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);

        user.getReadBooks().add(book);
        book.getReaders().add(user);
        book.getReviews().add(review);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.deleteById(1L);

        verify(userRepository).save(user);
        verify(reviewRepository).delete(review);
    }

    /* =========================
       findReviewsWithComments()
       ========================= */

    @Test
    void shouldReturnReviewsWithNonEmptyComments() {
        Review review = new Review();
        review.setComment("Nice");

        User user = new User();
        user.setUsername("user");
        user.setId(1L);

        Book book = new Book();
        book.setTitle("Book");
        book.setId(1L);

        review.setUser(user);
        review.setBook(book);

        when(reviewRepository.findAllByBookIdAndCommentIsNotNull(1L))
                .thenReturn(List.of(review));

        List<ReviewDTO> result = reviewService.findReviewsWithComments(1L);

        assertEquals(1, result.size());
    }
}