package com.helpers.notification.manager.parser;

import com.helpers.notification.filters.NotificationFilter;
import com.helpers.notification.handlers.NotificationHandler;

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
