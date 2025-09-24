package io.github.mohitc.helpers.notification.manager.parser;

import io.github.mohitc.helpers.notification.filters.NotificationFilter;
import io.github.mohitc.helpers.notification.handlers.NotificationHandler;

import java.util.List;

//Parsed Configuration for a notification processor
public class NotificationProcessorConf {

  private NotificationHandler handler;

  private List<NotificationFilter> filters;

  public NotificationHandler getHandler() {
    return handler;
  }

  public List<NotificationFilter> getFilters() {
    return filters;
  }

  public void setFilters(List<NotificationFilter> filters) {
    this.filters = filters;
  }

  public void setHandler(NotificationHandler handler) {
    this.handler = handler;
  }
}
