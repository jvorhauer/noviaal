package nl.noviaal.exception;

import java.util.UUID;

public class VideoNotFoundException extends RuntimeException {
  public VideoNotFoundException(UUID id) {
    super("Video with ID " + id + " not found");
  }
}
