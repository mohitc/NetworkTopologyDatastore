package com.topology.primitives.properties.converters.impl;

import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.PropertyConverter;

public class BooleanConverter implements PropertyConverter<Boolean> {

  @Override
  public Boolean fromString(String input) throws PropertyException {
    if (input==null)
      throw new PropertyException("Input cannot be null");
    input = input.trim();
    if (input.equalsIgnoreCase("true")) {
      return true;
    } else if (input.equalsIgnoreCase("false")) {
      return false;
    }
    throw new PropertyException("Input " + input + " is not a valid boolean");
  }

  @Override
  public String toString(Boolean input) throws PropertyException {
    if (input==null)
      throw new PropertyException("Input cannot be null");
    return input?"true":"false";
  }
}
