package com.helpers.notification.filters.impl;

import com.helpers.notification.filters.NotificationFilter;
import com.helpers.notification.messages.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class EntityIDNotificationFilter implements NotificationFilter {

  private static final Logger log = LoggerFactory.getLogger(EntityIDNotificationFilter.class);

  private final Set<Integer> idSet;

  public EntityIDNotificationFilter(Set<Integer> idSet) {
    this.idSet = new HashSet<>();
    this.idSet.addAll(idSet);
  }

  public EntityIDNotificationFilter(int id) {
    this.idSet = new HashSet<>();
    this.idSet.add(id);
  }

  public EntityIDNotificationFilter(String[] params) {
    this.idSet = new HashSet<>();
    if ((params==null) || (params.length==0)) {
      log.error("Entity ID notification filter initialized with empty set");
    } else {
      for (String param: params) {
        try {
          idSet.add(Integer.parseInt(param));
        } catch (Exception e) {
          log.error("Parameter " + param + " not a valid ID, skipping");
        }
      }
    }
  }

  @Override
  public boolean doFilter(NotificationMessage message) {
    return ! (idSet.contains(message.getEntityID()));
  }
}
