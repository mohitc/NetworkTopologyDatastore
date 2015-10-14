package com.helpers.notification.handlers.log;

import com.helpers.notification.handlers.NotificationHandler;
import com.helpers.notification.messages.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogNotificationHandler implements NotificationHandler{

  private static final Logger log = LoggerFactory.getLogger(LogNotificationHandler.class);

  String prefix = "Notification";

  public LogNotificationHandler() {
    prefix = "Notification";
  }

  public LogNotificationHandler(String[] params) {
    if ((params!=null) && (params.length==1)) {
      prefix = params[0];
    } else {
      prefix = "Notification";
    }
  }

  @Override
  public void handle(NotificationMessage message) {
    log.info(prefix + ": " + message);
  }
}
