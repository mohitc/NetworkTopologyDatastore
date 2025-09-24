package io.github.mohitc.helpers.notification.filters.impl;

import io.github.mohitc.helpers.notification.filters.NotificationFilter;
import io.github.mohitc.helpers.notification.messages.NotificationMessage;
import io.github.mohitc.helpers.notification.messages.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class TypeNotificationFilter implements NotificationFilter {

  private static final Logger log = LoggerFactory.getLogger(TypeNotificationFilter.class);

  private NotificationType supportedType;

  public TypeNotificationFilter(NotificationType supportedType) {
    this.supportedType = Objects.requireNonNullElse(supportedType, NotificationType.TENotification);
  }

  public TypeNotificationFilter(String[] params) {
    //Default action
    this.supportedType = NotificationType.TENotification;
    if ((params==null) || (params.length==0)) {
      log.info("Defaulting to TENotification filter");
    }
    else if ((params.length>1) || params[0] == null) {
      log.error("Invalid parameters provided to initialize the TypeNotificationFilter. Defaulting to TEnotification");
    } else {
      try {
        this.supportedType= NotificationType.valueOf(params[0].trim());
      } catch (IllegalArgumentException e) {
        log.error("Parameter {} is an invalid notification Type. Defaulting to TENotification", params[0]);
      }
    }
  }

  @Override
  public boolean doFilter(NotificationMessage message) {
    return ! ( message.getNotificationType().equals(supportedType) ||
                message.getNotificationType().getAllParents().contains(supportedType) );
  }
}
