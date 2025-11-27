package pl.melkowskiphilip.GoodReadsPL.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.melkowskiphilip.GoodReadsPL.exception.custom.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // - obsluguje wszystkie kontrolery, @Rest mowi ze zwracamy Response Body JSON
public class ValidationExceptionHandler {


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }

    @ExceptionHandler(AccountNotActivatedException.class)
    public ResponseEntity<String> handleAccountNotActivated(AccountNotActivatedException ex) {
        return ResponseEntity.status(403).body(ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<String> handleEmailUsed(EmailAlreadyUsedException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyUsedException.class)
    public ResponseEntity<String> handleUsernameUsed(UsernameAlreadyUsedException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AccountAlreadyActivatedException.class)
    public ResponseEntity<String> handleAccountAlreadyActive(AccountAlreadyActivatedException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }



    // jesli gdzies wystapi blad MethArgNValExc - to zrob to w przypadku adnotacji @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors); // zwroci jsona

    }

    // wywolywane np. dla dodania autora ktory juz jest w bazie
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }

    // dla nieaktywnego konta.
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> handleDisabled(DisabledException ex)
    {
        return ResponseEntity.status(403).body(ex.getMessage());
    }

    // złe dane logowania
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex)
    {
        return ResponseEntity.status(401).body(ex.getMessage());
    }

    // np. przy niepowodzeniu przy wyslaniu maila
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex)
    {
        return ResponseEntity.status(500).body(ex.getMessage());
    }

}
