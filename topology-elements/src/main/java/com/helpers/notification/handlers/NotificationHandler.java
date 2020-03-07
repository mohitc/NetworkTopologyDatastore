package com.helpers.notification.handlers;

import com.helpers.notification.messages.NotificationMessage;

//Interface for defining capabilities of methods to handle notifications
public interface NotificationHandler {

  void handle(NotificationMessage message);
}
