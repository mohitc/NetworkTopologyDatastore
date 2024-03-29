package com.helpers.notification.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;

public class NotificationException extends Exception {

  @Serial
  private static final long serialVersionUID = 1370403232109717641L;

  private static final Logger log = LoggerFactory.getLogger(NotificationException.class);

  private final String message;

  public NotificationException (String message) {
    log.error("[" + this.getClass() + "]" + message);
    this.message = message;
  }

  public String getMessage() {
    log.error(message);
    return message;
  }
}
