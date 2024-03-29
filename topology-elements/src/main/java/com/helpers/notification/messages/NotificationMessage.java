package com.helpers.notification.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.helpers.notification.exception.NotificationException;
import com.topology.dto.TopologyDTO;

//Class to present notification messages to external entities
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationMessage {

  private final NotificationType notificationType;

  private final long timestamp;

  private final int entityID;

  private final TopologyDTO oldState;

  private final TopologyDTO newState;

  public NotificationMessage(NotificationType type, int entityID, TopologyDTO oldState, TopologyDTO newState) throws NotificationException {
    if (type == null)
      throw new NotificationException("Notification type must be non-null");
    if ((oldState==null)&&(newState==null)) {
      throw  new NotificationException("At least one state (old/new) must be non-null");
    }
    this.notificationType = type;
    this.entityID = entityID;
    this.oldState = oldState;
    this.newState = newState;
    this.timestamp = System.currentTimeMillis();
  }

  public NotificationType getNotificationType() {
    return notificationType;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public int getEntityID() {
    return entityID;
  }

  public TopologyDTO getOldState() {
    return oldState;
  }

  public TopologyDTO getNewState() {
    return newState;
  }

  public String toString() {
    String out = "Type: " + notificationType + ", ID: " + entityID + ", Timestamp: " + timestamp;
    if (oldState!=null) {
      out = out + ", Old State: [" + oldState + "]";
    }
    if (newState!=null) {
      out = out + ", New State: [" + newState + "]";
    }
    return out;
  }
}
