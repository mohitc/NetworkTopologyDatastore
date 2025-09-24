package io.github.mohitc.helpers.notification.handlers;

import io.github.mohitc.helpers.notification.messages.NotificationMessage;

//Interface for defining capabilities of methods to handle notifications
public interface NotificationHandler {

  void handle(NotificationMessage message);
}
