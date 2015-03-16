package com.helpers.notification.handlers.impl;

import com.helpers.notification.handlers.NotificationHandler;
import com.helpers.notification.messages.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Basic notification handler to log notification messages
public class LogNotificationHandler implements NotificationHandler {

  private static final Logger log = LoggerFactory.getLogger(LogNotificationHandler.class);

  @Override
  public void handle(NotificationMessage message) {
    log.info("Notification: " + message);
  }
}
