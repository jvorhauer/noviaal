package nl.noviaal.controller;

import nl.noviaal.exception.EmailAddressInUseException;
import nl.noviaal.exception.InvalidCommand;
import nl.noviaal.exception.MediaInvalidException;
import nl.noviaal.exception.NoteNotFoundException;
import nl.noviaal.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(value = InvalidCommand.class)
  public ResponseEntity<Object> exception(InvalidCommand e) {
    return ResponseEntity.badRequest()
             .contentType(MediaType.TEXT_PLAIN)
             .body(e.getMessage());
  }

  @ExceptionHandler(value = EmailAddressInUseException.class)
  public ResponseEntity<Object> exception(EmailAddressInUseException e) {
    return ResponseEntity.badRequest()
             .contentType(MediaType.TEXT_PLAIN)
             .body(e.getMessage());
  }

  @ExceptionHandler(value = UserNotFoundException.class)
  public ResponseEntity<Object> exception(UserNotFoundException e) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(value = NoteNotFoundException.class)
  public ResponseEntity<?> exceptioon(NoteNotFoundException e) {
    return exception(HttpStatus.NOT_FOUND, e);
  }

  @ExceptionHandler(value = UsernameNotFoundException.class)
  public ResponseEntity<?> exception(UsernameNotFoundException e) {
    return exception(HttpStatus.UNAUTHORIZED, e);
  }

  @ExceptionHandler(value = MediaInvalidException.class)
  public ResponseEntity<?> exception(MediaInvalidException e) {
    return exception(HttpStatus.BAD_REQUEST, e);
  }

  private ResponseEntity<?> exception(HttpStatus status, RuntimeException e) {
    return ResponseEntity.status(status).body(e.getMessage());
  }
}
