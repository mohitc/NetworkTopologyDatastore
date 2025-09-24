package io.github.mohitc.topology.impl.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface JdoClass {
  String value() default "";
}
