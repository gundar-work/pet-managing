package ia.mdotm.controller.error;

import ia.mdotm.service.PetNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(PetNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(PetNotFoundException ex, HttpServletRequest request) {

    ErrorResponse body = ErrorResponse.of(
      HttpStatus.NOT_FOUND.value(),
      HttpStatus.NOT_FOUND.getReasonPhrase(),
      ex.getMessage(),
      request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

    Map<String, String> fieldErrors = new LinkedHashMap<>();
    for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
      fieldErrors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
    }

    ErrorResponse body = ErrorResponse.of(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "Validation failed",
      request.getRequestURI(),
      Map.of("fieldErrors", fieldErrors)
    );

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest request) {

    ErrorResponse body = ErrorResponse.of(
      HttpStatus.BAD_REQUEST.value(),
      HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "Malformed JSON request",
      request.getRequestURI()
    );
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {

    ErrorResponse body = ErrorResponse.of(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
      ex.getMessage(),
      request.getRequestURI()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }
}

