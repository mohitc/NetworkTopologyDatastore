package com.topology.primitives.properties.converters.impl;

import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.PropertyConverter;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
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
