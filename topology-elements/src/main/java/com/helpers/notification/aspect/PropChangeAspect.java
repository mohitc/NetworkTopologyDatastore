package com.helpers.notification.aspect;

import com.helpers.notification.annotation.PropChange;
import com.helpers.notification.exception.NotificationException;
import com.helpers.notification.manager.NotificationManager;
import com.helpers.notification.messages.NotificationMessage;
import com.helpers.notification.messages.NotificationType;
import com.topology.dto.*;
import com.topology.primitives.TopologyElement;
import com.topology.primitives.exception.TopologyException;
import com.topology.primitives.exception.properties.PropertyException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class PropChangeAspect {

  private static final Logger log = LoggerFactory.getLogger(PropChangeAspect.class);

  @Around("execution(* *(..)) && @annotation(annotation)")
  public Object propertyChangeDetection(ProceedingJoinPoint call, PropChange annotation) throws Throwable {
    boolean catchException = false;
    if (TopologyElement.class.isAssignableFrom(call.getThis().getClass())) {
      TopologyDTO beforeChange = TopologyDTO.generateDTO((TopologyElement) call.getThis());
      try {
        return call.proceed();
      } catch (Exception e) {
        //potential problems in generating notification, likely that property change failed
        log.error("Error while changing property", e);
        catchException = true;
        throw e;
      } finally {
        if (!catchException) {
          try{
            TopologyDTO afterChange = TopologyDTO.generateDTO((TopologyElement) call.getThis());
            NotificationType type = getNotificationType(afterChange.getClass());
            NotificationMessage message = new NotificationMessage(type, afterChange.getId(), beforeChange, afterChange);
            NotificationManager.receiveNotification(message);
          } catch (NotificationException e) {
            log.error("Error in generating Notification Message: " + e);
          } catch (PropertyException e) {
            log.error("Error in generating DTO for class: "+ call.getThis());
          }
        }
      }
    } else {
      throw new TopologyException("PropChange annotation was used in a location not suited for generating notifications");
    }
  }

  private <T extends TopologyDTO> NotificationType getNotificationType(Class<T> instance) {
    NotificationType type = null;
    if (NetworkElementDTO.class.isAssignableFrom(instance)) {
      type = NotificationType.NEModificationNotification;
    } else if (ConnectionDTO.class.isAssignableFrom(instance)) {
      type = NotificationType.ConnModificationNotification;
      if (LinkDTO.class.isAssignableFrom(instance)) {
        type = NotificationType.LinkModificationNotification;
      } else if (CrossConnectDTO.class.isAssignableFrom(instance)) {
        type = NotificationType.CrsCcModificationNotification;
      }
    } else if (ConnectionPointDTO.class.isAssignableFrom(instance)) {
      type = NotificationType.CPModificationNotification;
      if (PortDTO.class.isAssignableFrom(instance)) {
        type = NotificationType.PortModificationNotification;
      }
    }
    return type;
  }

}
