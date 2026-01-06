package pl.melkowskiphilip.GoodReadsPL.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.melkowskiphilip.GoodReadsPL.dto.ReviewDTO;
import pl.melkowskiphilip.GoodReadsPL.entity.User;
import pl.melkowskiphilip.GoodReadsPL.service.ReviewService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // zwraca wszystkie recenzje
    @GetMapping("/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() { return ResponseEntity.ok(reviewService.findAll()); }

    @GetMapping("/allByBook/{id}")
    public ResponseEntity<List<ReviewDTO>> getAllByBook(@PathVariable Long id) { return ResponseEntity.ok(reviewService.findAllByBook(id)); }

    @GetMapping("/me")
    public ResponseEntity<List<ReviewDTO>> getAllByUser(Authentication auth) { return ResponseEntity.ok(reviewService.findAllByCurrentUser(auth)); }

    @GetMapping("getByUserAndBook/{bookId}/{userId}")
    public ResponseEntity<ReviewDTO> getByUserAndBook(@PathVariable Long bookId, @PathVariable Long userId) { return ResponseEntity.ok(reviewService.findByBookAndUser(bookId, userId)); }

    //dodaj recenzje jesli jeszcze nie istnieje
    @PostMapping()
    public ResponseEntity<ReviewDTO> saveReview(@Valid @RequestBody ReviewDTO newReview
    ,Authentication auth)
    {
        User user = (User) auth.getPrincipal();
        Long userId = user.getId();
        return ResponseEntity.ok(reviewService.saveReview(newReview, userId));
    }

    // aktualizuje dana recenzje
    @PutMapping()
    public ResponseEntity<ReviewDTO> updateReview(@Valid @RequestBody ReviewDTO updatedReview)
    {
        return ResponseEntity.ok(reviewService.updateReview(updatedReview));
    }

    // usuwa dana recenzje
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) { reviewService.deleteById(id); return ResponseEntity.noContent().build(); }

    // recenzje z komentarzem dla danej ksiazki
    @GetMapping("/comments/{bookId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsWithComments(@PathVariable Long bookId)
    {
        return ResponseEntity.ok(reviewService.findReviewsWithComments(bookId));
    }

    // recenzje danej ksiazki o danej ocenie ktore maja komentarz
    @GetMapping("/commentsByRating/{bookId}/{rating}")
    public ResponseEntity<List<ReviewDTO>> getReviewsWithCommentsByRating(@PathVariable Long bookId, @PathVariable int rating)
    {
        return ResponseEntity.ok(reviewService.findReviewsWithCommentsByRating(bookId, rating));
    }
}
