package com.topology.impl.db.primitives.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.util.HashMap;
import java.util.Map;

public class ObjectClassConverter implements AttributeConverter<Class, String> {

  private static final Logger log = LoggerFactory.getLogger(ObjectClassConverter.class);

  private static final Map<String, Class> classMap = new HashMap<>();

  @Override
  public String convertToDatabaseColumn(Class aClass) {
    if (aClass!=null)
      return aClass.getName();
    log.error("Class to converter is null");
    return "";
  }

  @Override
  public Class convertToEntityAttribute(String s) {
    if (s==null) {
      log.error("Could not load class with empty parameter");
      return null;
    }
    try {
      if (classMap.containsKey(s))
        return classMap.get(s);
      Class aClass = Class.forName(s);
      classMap.put(s, aClass);
      return aClass;
    } catch (ClassNotFoundException e) {
      log.error("Could not load class", e);
      return null;
    }
  }
}
