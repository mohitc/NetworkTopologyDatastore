package com.topology.primitives.properties.converters.impl;

import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.PropertyConverter;

public class LongConverter implements PropertyConverter<Long>{
  @Override
  public Long fromString(String input) throws PropertyException {
    if (input==null)
      throw new PropertyException("Input cannot be null");
    try {
      return Long.parseLong(input.trim());
    } catch (NumberFormatException e) {
      throw new PropertyException("Input " + input + " is not a valid long string");
    }
  }

  @Override
  public String toString(Long input) throws PropertyException {
    if (input==null)
      throw new PropertyException("Input cannot be null");
    return Long.toString(input);
  }
}
