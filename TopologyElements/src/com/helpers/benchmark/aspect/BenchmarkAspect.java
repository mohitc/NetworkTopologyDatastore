package com.helpers.benchmark.aspect;

import com.helpers.benchmark.annotation.Benchmark;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class BenchmarkAspect {
  @Around("execution(* *(..)) && @annotation(annotation)")
  public Object persistenceContext(ProceedingJoinPoint call, Benchmark annotation) throws Throwable {
    long startTime = System.currentTimeMillis();
    try {
      return call.proceed();
    } finally {
      long endTime = System.currentTimeMillis();
      System.out.println(call.getSignature().getName()+ "\t" + (endTime-startTime));
    }
  }
}
