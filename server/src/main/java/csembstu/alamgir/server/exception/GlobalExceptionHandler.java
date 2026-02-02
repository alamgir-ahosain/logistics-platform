package csembstu.alamgir.server.exception;

import java.util.stream.Collectors;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

// @ControllerAdvice
// public class GlobalExceptionHandling {

//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

//         Map<String, Object> body = new LinkedHashMap<>();
//         body.put("timestamp", LocalDateTime.now());
//         body.put("status", HttpStatus.BAD_REQUEST.value());
//         body.put("error", "Validation Failed");

//         // Specific field errors
//         Map<String, String> errors = new HashMap<>();
//         ex.getBindingResult().getFieldErrors().forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));
//         body.put("errors", errors);

//         return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//     }

// }

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        ErrorResponse error = new ErrorResponse("BAD_REQUEST", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", message));
    }
}
