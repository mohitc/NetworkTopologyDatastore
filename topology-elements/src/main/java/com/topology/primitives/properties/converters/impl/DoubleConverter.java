package com.topology.primitives.properties.converters.impl;

import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.PropertyConverter;

public class DoubleConverter implements PropertyConverter<Double> {

  @Override
  public Double fromString(String input) throws PropertyException {
    if (input==null)
      throw new PropertyException("Input cannot be null");
    try {
      return Double.parseDouble(input.trim());
    } catch (NumberFormatException e) {
      throw new PropertyException("Input " + input + " is not a valid double string");
    }
  }

  @Override
  public String toString(Double input) throws PropertyException {
    if (input==null)
      throw new PropertyException("Input cannot be null");
    return Double.toString(input);
  }
}
