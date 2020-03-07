package com.topology.primitives.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileFormatException extends Exception {

  private static final Logger log = LoggerFactory.getLogger(FileFormatException.class);

  private String message;

  public FileFormatException (String message) {
    log.error("[" + this.getClass() + "]" + message);
    this.message = message;
  }

  public String getMessage() {
    log.error(message);
    return message;
  }

}
