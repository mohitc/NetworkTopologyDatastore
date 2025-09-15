package io.github.mohitc.topology.primitives.properties.converters.impl;

import io.github.mohitc.topology.primitives.exception.properties.PropertyException;
import io.github.mohitc.topology.primitives.properties.converters.PropertyConverter;

public class BooleanConverter implements PropertyConverter<Boolean> {

  @Override
  public Boolean fromString(String input) throws PropertyException {
    if (input==null) {
      throw new PropertyException("Input cannot be null");
    }
    input = input.trim();
    if ("true".equalsIgnoreCase(input)) {
      return true;
    } else if ("false".equalsIgnoreCase(input)) {
      return false;
    }
    throw new PropertyException("Input " + input + " is not a valid boolean");
  }

  @Override
  public String toString(Boolean input) throws PropertyException {
    if (input==null) {
      throw new PropertyException("Input cannot be null");
    }
    return input?"true":"false";
  }
}
