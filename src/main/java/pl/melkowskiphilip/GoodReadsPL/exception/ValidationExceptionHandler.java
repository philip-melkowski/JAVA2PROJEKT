package pl.melkowskiphilip.GoodReadsPL.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // - obsluguje wszystkie kontrolery, @Rest mowi ze zwracamy Response Body JSON
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // jesli gdzies wystapi blad MethArgNValExc - to zrob to w przypadku adnotacji @Valid
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors); // zwroci jsona

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) { // wywolywane np. dla dodania autora ktory juz jest w bazie
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}
