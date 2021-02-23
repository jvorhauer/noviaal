package nl.noviaal.exception;

import java.util.UUID;

public class MediaNotFoundException extends RuntimeException {
  public MediaNotFoundException(UUID id) {
    super("Media with ID " + id + " not found");
  }
}
