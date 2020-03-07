package com.topology.primitives.properties;

import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.PropertyConverter;

public interface TEPropertyKey {

  String getId();

  String getDescription();

  Class getObjClass();

  Class<? extends PropertyConverter> getConverterClass();

  PropertyConverter getConverter() throws PropertyException;

}
