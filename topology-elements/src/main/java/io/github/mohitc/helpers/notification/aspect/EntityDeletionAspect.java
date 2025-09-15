package io.github.mohitc.helpers.notification.aspect;

import io.github.mohitc.helpers.notification.annotation.EntityDeletion;
import io.github.mohitc.helpers.notification.exception.NotificationException;
import io.github.mohitc.helpers.notification.manager.NotificationManager;
import io.github.mohitc.helpers.notification.messages.NotificationMessage;
import io.github.mohitc.helpers.notification.messages.NotificationType;
import io.github.mohitc.topology.dto.*;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import io.github.mohitc.topology.primitives.exception.properties.PropertyException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class EntityDeletionAspect {

  private static final Logger log = LoggerFactory.getLogger(EntityDeletionAspect.class);

  @Around("execution(* *(..)) && @annotation(annotation)")
  public Object deletionDetection(ProceedingJoinPoint call, EntityDeletion annotation) throws Throwable {
    boolean catchException = false;
    if (TopologyManager.class.isAssignableFrom(call.getThis().getClass()) &&
        (call.getArgs().length==1) &&
        Integer.class.isAssignableFrom(call.getArgs()[0].getClass())) {
      TopologyManager manager = (TopologyManager) call.getThis();
      int id = (Integer)call.getArgs()[0];
      TopologyDTO beforeChange = null;
      if (manager.hasElement(id)) {
        try {
          beforeChange = TopologyDTO.generateDTO(manager.getElementByID(id));
        } catch (PropertyException e) {
          log.error("Error in generating DTO for class:", e);
        }
      }
      try {
        return call.proceed();
      } catch (Exception e) {
        //potential problems in generating notification, likely that property change failed
        log.error("Error while deleting entity: ", e);
        catchException = true;
        throw e;
      } finally {
        if ((!catchException) && (beforeChange!=null)) {
          try{
            NotificationType type = getNotificationType(beforeChange.getClass());
            NotificationMessage message = new NotificationMessage(type, beforeChange.getId(), beforeChange, null);
            NotificationManager.receiveNotification(message);
          } catch (NotificationException e) {
            log.error("Error in generating Notification Message: ", e);
          }
        }
      }
    } else {
      throw new TopologyException("EntityDeletion annotation was used in a location not suited for generating notifications");
    }
  }



  private <T extends TopologyDTO> NotificationType getNotificationType(Class<T> instance) {
    NotificationType type = null;
    if (NetworkElementDTO.class.isAssignableFrom(instance)) {
      type = NotificationType.NEDeletionNotification;
    } else if (ConnectionDTO.class.isAssignableFrom(instance)) {
      type = NotificationType.ConnDeletionNotification;
      if (LinkDTO.class.isAssignableFrom(instance)) {
        type = NotificationType.LinkDeletionNotification;
      } else if (CrossConnectDTO.class.isAssignableFrom(instance)) {
        type = NotificationType.CrsCcDeletionNotification;
      }
    } else if (ConnectionPointDTO.class.isAssignableFrom(instance)) {
      type=NotificationType.CPDeletionNotification;
      if (PortDTO.class.isAssignableFrom(instance)){
        type = NotificationType.PortDeletionNotification;
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
