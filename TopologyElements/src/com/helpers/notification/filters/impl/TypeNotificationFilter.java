package com.helpers.notification.filters.impl;

import com.helpers.notification.filters.NotificationFilter;
import com.helpers.notification.messages.NotificationMessage;
import com.helpers.notification.messages.NotificationType;

public class TypeNotificationFilter implements NotificationFilter {

  private NotificationType supportedType;

  public TypeNotificationFilter(NotificationType supportedType) {
    if (supportedType==null) {
      this.supportedType = NotificationType.TENotification;
    } else
      this.supportedType = supportedType;
  }

  @Override
  public boolean doFilter(NotificationMessage message) {
    return (! ( message.getNotificationType().equals(supportedType) ||
                message.getNotificationType().getAllParents().contains(supportedType) ) ) ;
  }
}
