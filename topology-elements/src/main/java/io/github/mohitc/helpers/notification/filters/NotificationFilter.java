package io.github.mohitc.helpers.notification.filters;

import io.github.mohitc.helpers.notification.messages.NotificationMessage;

public interface NotificationFilter {

  /** Function to identify if a message should be filtered or not
   *
   * @param message Message on which filter should be applied
   * @return Boolean (true indicates message should not be forwarded / false indicates message should be forwarded)
   */
  boolean doFilter(NotificationMessage message);

}
