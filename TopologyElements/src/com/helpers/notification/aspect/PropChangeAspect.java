package com.helpers.notification.aspect;

import com.helpers.notification.annotation.PropChange;
import com.topology.dto.TopologyDTO;
import com.topology.primitives.TopologyElement;
import com.topology.primitives.exception.TopologyException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class PropChangeAspect {

  private static final Logger log = LoggerFactory.getLogger(PropChangeAspect.class);

  @Around("execution(* *(..)) && @annotation(annotation)")
  public Object persistenceContext(ProceedingJoinPoint call, PropChange annotation) throws Throwable {
    if (TopologyElement.class.isAssignableFrom(call.getThis().getClass())) {
      try {
        TopologyDTO beforeChange = TopologyDTO.generateDTO((TopologyElement) call.getThis());
        try {
          return call.proceed();
        } finally {
          TopologyDTO afterChange = TopologyDTO.generateDTO((TopologyElement) call.getThis());
          System.out.println("Property Change: before [" + beforeChange + "] after [" + afterChange + "]");
        }
      } catch (Exception e) {
        //potential problems in generating notification, likely that property change failed
        log.error("Error while changing property", e);
        throw e;
      }
    } else {
      throw new TopologyException("PropChange annotation was used in a location not suited for generating notifications");
    }
  }
}
