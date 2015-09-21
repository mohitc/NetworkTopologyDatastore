package com.topology.primitives.properties;

import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.PropertyConverter;

public interface TEPropertyKey {

  public String getId();

  public String getDescription();

  public Class getObjClass();

  public Class<? extends PropertyConverter> getConverterClass();

  public PropertyConverter getConverter() throws PropertyException;

}
