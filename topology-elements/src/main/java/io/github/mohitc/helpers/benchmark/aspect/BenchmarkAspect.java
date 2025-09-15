package io.github.mohitc.helpers.benchmark.aspect;

import io.github.mohitc.helpers.benchmark.annotation.Benchmark;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class BenchmarkAspect {

  private static final Logger log = LoggerFactory.getLogger(BenchmarkAspect.class);

  @Around("execution(* *(..)) && @annotation(annotation)")
  public Object persistenceContext(ProceedingJoinPoint call, Benchmark annotation) throws Throwable {
    long startTime = System.currentTimeMillis();
    try {
      return call.proceed();
    } finally {
      long endTime = System.currentTimeMillis();
        if (log.isInfoEnabled()) {
          log.info("{}\t{}", call.getSignature().getName(), endTime - startTime);
        }
    }
  }
}
