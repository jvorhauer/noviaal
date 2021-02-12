package nl.noviaal.exception;

public class NoteNotFoundException extends RuntimeException {
  public NoteNotFoundException(String msg) {
    super(msg);
  }
}
