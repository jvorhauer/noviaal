package nl.noviaal.exception;

public class InvalidCommand extends RuntimeException {
  public InvalidCommand(String type) {
    super("Error performing " + type);
  }
}
