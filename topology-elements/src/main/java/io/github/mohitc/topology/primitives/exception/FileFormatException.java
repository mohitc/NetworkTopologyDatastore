package io.github.mohitc.topology.primitives.exception;

public class FileFormatException extends Exception {

  private final String message;

  public FileFormatException (String message) {
    this.message = message;
  }

  public FileFormatException(Throwable e) {
    super(e);
    this.message = e.getMessage();
  }

  @Override
  public String getMessage() {
    return message;
  }

}
