package nl.noviaal.exception;

public class EmailAddressInUseException extends RuntimeException {
  public EmailAddressInUseException(String email) {
    super("Email address " + email + " already in use");
  }
}
