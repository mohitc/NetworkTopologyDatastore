package com.helpers.notification.filters;

import com.helpers.notification.messages.NotificationMessage;

public interface NotificationFilter {

  /** Function to identify if a message should be filtered or not
   *
   * @param message Message on which filter should be applied
   * @return Boolean (true indicates message should not be forwarded / false indicates message should be forwarded)
   */
  public boolean doFilter(NotificationMessage message);

}
