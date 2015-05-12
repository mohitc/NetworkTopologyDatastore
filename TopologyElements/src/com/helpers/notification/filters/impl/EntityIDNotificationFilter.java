package com.helpers.notification.filters.impl;

import com.helpers.notification.filters.NotificationFilter;
import com.helpers.notification.messages.NotificationMessage;

import java.util.HashSet;
import java.util.Set;

public class EntityIDNotificationFilter implements NotificationFilter {

  private Set<Integer> idSet;

  public EntityIDNotificationFilter(Set<Integer> idSet) {
    this.idSet = new HashSet<>();
    this.idSet.addAll(idSet);
  }

  public EntityIDNotificationFilter(int id) {
    this.idSet = new HashSet<>();
    this.idSet.add(id);
  }

  @Override
  public boolean doFilter(NotificationMessage message) {
    return ! (idSet.contains(message.getEntityID()));
  }
}
