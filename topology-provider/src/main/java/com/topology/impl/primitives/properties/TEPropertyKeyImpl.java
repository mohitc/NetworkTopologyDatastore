package com.topology.impl.primitives.properties;

import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.TEPropertyKey;
import com.topology.primitives.properties.converters.PropertyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class TEPropertyKeyImpl implements TEPropertyKey {

  private static final Logger log = LoggerFactory.getLogger(TEPropertyKeyImpl.class);

  private final String id;

  private final String description;

  private final Class objClass;

  private final Class<? extends PropertyConverter> converterClass;

  public TEPropertyKeyImpl(String id, String description, Class objClass, Class<? extends PropertyConverter> converterClass) {
    this.id = id;
    this.description = description;
    this.objClass = objClass;
    this.converterClass = converterClass;
  }

  public String getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public Class getObjClass() {
    return objClass;
  }

  @Override
  public Class<? extends PropertyConverter> getConverterClass() {
    return converterClass;
  }

  @Override
  public PropertyConverter getConverter() throws PropertyException {
    try {
      return converterClass.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      log.error("Error while creating property converter instance", e);
      throw new PropertyException("Error while creating property converter instance: " + e.getMessage());
    }
  }

  public boolean equals(Object o) {
    if (o!=null) {
      if (o instanceof TEPropertyKey) {
        TEPropertyKey key = (TEPropertyKey)o;
        return (key.getId().equals(this.getId()) && key.getObjClass().equals(this.getObjClass()));
      }
    }
    return false;
  }

  public int hashCode() {
    return id.hashCode() * 31 + objClass.hashCode();
  }

}
