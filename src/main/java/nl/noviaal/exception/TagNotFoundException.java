package nl.noviaal.exception;

public class TagNotFoundException extends RuntimeException {
  public TagNotFoundException(String msg) {
    super(msg);
  }
}
