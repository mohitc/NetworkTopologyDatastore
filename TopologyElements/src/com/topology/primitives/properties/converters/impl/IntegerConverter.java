package com.topology.primitives.properties.converters.impl;

import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.PropertyConverter;

public class IntegerConverter implements PropertyConverter<Integer> {

  @Override
  public Integer fromString(String input) throws PropertyException {
    if (input==null)
      throw new PropertyException("Input cannot be null");
    try {
      return Integer.parseInt(input.trim());
    } catch (NumberFormatException e) {
      throw new PropertyException("Input " + input + " is not a valid integer string");
    }
  }

  @Override
  public String toString(Integer input) throws PropertyException {
    if (input==null)
      throw new PropertyException("Input cannot be null");
    return Integer.toString(input);
  }
}
