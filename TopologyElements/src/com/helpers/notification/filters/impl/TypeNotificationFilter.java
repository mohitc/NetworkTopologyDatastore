package com.helpers.notification.filters.impl;

import com.helpers.notification.filters.NotificationFilter;
import com.helpers.notification.messages.NotificationMessage;
import com.helpers.notification.messages.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeNotificationFilter implements NotificationFilter {

  private static final Logger log = LoggerFactory.getLogger(TypeNotificationFilter.class);

  private NotificationType supportedType;

  public TypeNotificationFilter(NotificationType supportedType) {
    if (supportedType==null) {
      this.supportedType = NotificationType.TENotification;
    } else
      this.supportedType = supportedType;
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
        log.error("Parameter " + params[0] + " is an invalid notification Type. Defaulting to TENotification");
      }
    }
  }

  @Override
  public boolean doFilter(NotificationMessage message) {
    return (! ( message.getNotificationType().equals(supportedType) ||
                message.getNotificationType().getAllParents().contains(supportedType) ) ) ;
  }
}
