package com.helpers.notification.aspect;

import com.helpers.notification.annotation.EntityCreation;
import com.helpers.notification.exception.NotificationException;
import com.helpers.notification.manager.NotificationManager;
import com.helpers.notification.messages.NotificationMessage;
import com.helpers.notification.messages.NotificationType;
import com.topology.dto.*;
import com.topology.primitives.TopologyElement;
import com.topology.primitives.exception.properties.PropertyException;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class EntityCreationAspect {

  private static final Logger log = LoggerFactory.getLogger(EntityCreationAspect.class);

  @AfterReturning(pointcut = "execution(* *(..)) && @annotation(annotation)",  returning="out")
  public void generateCreationNotification(EntityCreation annotation, TopologyElement out) {
    try {
      TopologyDTO dto = TopologyDTO.generateDTO(out);
      NotificationType type = getNotificationType(dto.getClass());
      if (type!=null) {
        try {
          NotificationMessage message = new NotificationMessage(type, dto.getId(), null, dto);
          NotificationManager.receiveNotification(message);
        } catch (NotificationException e) {
          log.error("Error in generating notification message:", e);
        }
      } else {
        log.error("Could not correctly identify notification type from DTO: " + dto);
      }
    } catch (PropertyException e) {
      log.error(e.getMessage(), e);
    }
  }

  private <T extends TopologyDTO> NotificationType getNotificationType(Class<T> instance) {
    NotificationType type = null;
    if (NetworkElementDTO.class.isAssignableFrom(instance)) {
      type = NotificationType.NECreationNotification;
    } else if (ConnectionDTO.class.isAssignableFrom(instance)) {
      type = NotificationType.ConnCreationNotification;
      if (LinkDTO.class.isAssignableFrom(instance)) {
        type = NotificationType.LinkCreationNotification;
      } else if (CrossConnectDTO.class.isAssignableFrom(instance)) {
        type = NotificationType.CrsCcCreationNotification;
      }
    } else if (ConnectionPointDTO.class.isAssignableFrom(instance)) {
      type=NotificationType.CPCreationNotification;
      if (PortDTO.class.isAssignableFrom(instance)){
        type = NotificationType.PortCreationNotification;
      }
    }
    return type;
  }


/*
  @Around("execution(* *(..)) && @annotation(annotation)")
  public Object persistenceContext(ProceedingJoinPoint call, EntityCreation annotation) throws Throwable {
    boolean catchException = false;
    if (TopologyElement.class.isAssignableFrom(call.getThis().getClass())) {
      TopologyDTO beforeChange = TopologyDTO.generateDTO((TopologyElement) call.getThis());
      try {
        call.getTarget()
        return call.proceed();
      } catch (Exception e) {
        //potential problems in generating notification, likely that property change failed
        log.error("Error while changing property", e);
        catchException = true;
        throw e;
      } finally {
        if (!catchException) {
          TopologyDTO afterChange = TopologyDTO.generateDTO((TopologyElement) call.getThis());
          System.out.println("Property Change: before [" + beforeChange + "] after [" + afterChange + "]");
        }
      }
    } else {
      throw new TopologyException("PropChange annotation was used in a location not suited for generating notifications");
    }
  }
*/

}
