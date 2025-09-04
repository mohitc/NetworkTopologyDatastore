package com.topology.impl.db.primitives.converters;

import com.topology.primitives.properties.converters.PropertyConverter;
import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ConverterClassConverter implements AttributeConverter<Class<? extends PropertyConverter>, String> {

  private static final Logger log = LoggerFactory.getLogger(ConverterClassConverter.class);

  private static final Map<String, Class> classMap = new HashMap<>();

  @Override
  public String convertToDatabaseColumn(Class<? extends PropertyConverter> aClass) {
    if (aClass!=null)
      return aClass.getName();
    log.error("Class to converter is null");
    return "";
  }

  @Override
  public Class<? extends PropertyConverter> convertToEntityAttribute(String s) {
    if (s==null) {
      log.error("Could not load class with empty parameter");
      return null;
    }
    try {
      if (classMap.containsKey(s))
        return classMap.get(s);
      Class aClass = Class.forName(s);
      if (PropertyConverter.class.isAssignableFrom(aClass)) {
        classMap.put(s, aClass);
        return aClass;
      } else {
        log.error("Class " + aClass.getName() + " not a valid property converter");
        return null;
      }
    } catch (ClassNotFoundException e) {
      log.error("Could not load class", e);
      return null;
    }
  }
}
