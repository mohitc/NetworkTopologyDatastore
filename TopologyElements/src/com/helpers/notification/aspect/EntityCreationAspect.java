package com.helpers.notification.aspect;

import com.helpers.notification.annotation.EntityCreation;
import com.helpers.notification.messages.NotificationType;
import com.topology.dto.TopologyDTO;
import com.topology.primitives.TopologyElement;
import com.topology.primitives.exception.TopologyException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class EntityCreationAspect {

  @AfterReturning(pointcut = "execution(* *(..)) && @annotation(annotation)",  returning="out")
  public void topologyElement(EntityCreation annotation, TopologyElement out) {
    System.out.println("It's a Foo: " + out.getClass().getSimpleName() + ":" + out + NotificationType.TECreationNotification.getStrFormat());
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
