package io.github.mohitc.topology.primitives.properties.converters.impl;

import io.github.mohitc.topology.primitives.exception.properties.PropertyException;
import io.github.mohitc.topology.primitives.properties.converters.PropertyConverter;

public class IntegerConverter implements PropertyConverter<Integer> {

  @Override
  public Integer fromString(String input) throws PropertyException {
    if (input==null) {
      throw new PropertyException("Input cannot be null");
    }
    try {
      return Integer.parseInt(input.trim());
    } catch (NumberFormatException e) {
      throw new PropertyException("Input " + input + " is not a valid integer string", e);
    }
  }

  @Override
  public String toString(Integer input) throws PropertyException {
    if (input==null) {
      throw new PropertyException("Input cannot be null");
    }
    return Integer.toString(input);
  }
}
