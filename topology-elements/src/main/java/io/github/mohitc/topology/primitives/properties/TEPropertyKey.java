package io.github.mohitc.topology.primitives.properties;

import io.github.mohitc.topology.primitives.exception.properties.PropertyException;
import io.github.mohitc.topology.primitives.properties.converters.PropertyConverter;

public interface TEPropertyKey {

  String id();

  String description();

  Class objClass();

  Class<? extends PropertyConverter> converterClass();

  PropertyConverter getConverter() throws PropertyException;

}
