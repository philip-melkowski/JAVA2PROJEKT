package pl.melkowskiphilip.GoodReadsPL.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    private final MessageSource messageSource;

    public ValidationExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // ========= AUTH / USER =========

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(
            UserNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(404)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(
            InvalidCredentialsException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(401)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(AccountNotActivatedException.class)
    public ResponseEntity<String> handleAccountNotActivated(
            AccountNotActivatedException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(403)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<String> handleEmailUsed(
            EmailAlreadyUsedException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(409)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(UsernameAlreadyUsedException.class)
    public ResponseEntity<String> handleUsernameUsed(
            UsernameAlreadyUsedException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(409)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(AccountAlreadyActivatedException.class)
    public ResponseEntity<String> handleAccountAlreadyActive(
            AccountAlreadyActivatedException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(409)
                .body(resolveMessage(ex.getMessage(), request));
    }

    // ========= TOKEN =========

    @ExceptionHandler(InvalidActivationTokenException.class)
    public ResponseEntity<String> handleInvalidActivationToken(
            InvalidActivationTokenException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(400)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(ActivationTokenExpiredException.class)
    public ResponseEntity<String> handleActivationTokenExpired(
            ActivationTokenExpiredException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(410)
                .body(resolveMessage(ex.getMessage(), request));
    }

    // ========= VALIDATION =========

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    // ========= SECURITY =========

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> handleDisabled(
            DisabledException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(403)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(401)
                .body(resolveMessage(ex.getMessage(), request));
    }

    // ========= BOOK / AUTHOR / REVIEW =========

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBookNotFound(
            BookNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(404)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<String> handleBookExists(
            BookAlreadyExistsException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(409)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<String> handleAuthorNotFound(
            AuthorNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(404)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(AuthorAlreadyExistsException.class)
    public ResponseEntity<String> handleAuthorExists(
            AuthorAlreadyExistsException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(409)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<String> handleReviewNotFound(
            ReviewNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(404)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<String> handleReviewExists(
            ReviewAlreadyExistsException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(409)
                .body(resolveMessage(ex.getMessage(), request));
    }

    @ExceptionHandler(InvalidReviewIdException.class)
    public ResponseEntity<String> handleInvalidReviewId(
            InvalidReviewIdException ex,
            HttpServletRequest request) {

        return ResponseEntity.badRequest()
                .body(resolveMessage(ex.getMessage(), request));
    }

    // ========= HELPER =========

    private String resolveMessage(String key, HttpServletRequest request) {
        Locale locale = request.getLocale();
        return messageSource.getMessage(key, null, key, locale);
    }
}