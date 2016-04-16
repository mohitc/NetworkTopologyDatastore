package com.topology.primitives.properties.converters.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.PropertyConverter;

import java.io.IOException;
import java.util.Map;

public class MapConverter implements PropertyConverter<Map> {

  @Override
  public Map fromString(String input) throws PropertyException {
    if (input==null)
      throw new PropertyException("Input cannot be null");
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(input, Map.class);
    } catch (IOException e) {
      throw new PropertyException("Error in converting string value to map property : " + e.getMessage());
    }
  }

  @Override
  public String toString(Map input) throws PropertyException {
    if (input==null)
      throw new PropertyException("Input cannot be null");
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(input);
    } catch (IOException e) {
      throw new PropertyException("Error in converting map property to JSON string: " + e.getMessage());
    }
  }
}
