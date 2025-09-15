package io.github.mohitc.topology.impl.primitives.properties;

import io.github.mohitc.topology.primitives.exception.properties.PropertyException;
import io.github.mohitc.topology.primitives.properties.TEPropertyKey;
import io.github.mohitc.topology.primitives.properties.converters.PropertyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public record TEPropertyKeyImpl(String id, String description, Class objClass,
                                Class<? extends PropertyConverter> converterClass) implements TEPropertyKey {

  private static final Logger log = LoggerFactory.getLogger(TEPropertyKeyImpl.class);

  @Override
  public PropertyConverter getConverter() throws PropertyException {
    try {
      return converterClass.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      log.error("Error while creating property converter instance", e);
      throw new PropertyException("Error while creating property converter instance", e);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o != null) {
      if (o instanceof TEPropertyKey key) {
        return key.id().equals(this.id()) && key.objClass().equals(this.objClass());
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    return id.hashCode() * 31 + objClass.hashCode();
  }

}
