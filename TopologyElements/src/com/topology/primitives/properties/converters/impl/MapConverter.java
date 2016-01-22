package com.topology.primitives.properties.converters.impl;

import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.PropertyConverter;

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
    try {
      Map<String, String> map = new HashMap<String, String>();

      String[] nameValuePairs = input.trim().split("&");
      for (String nameValuePair : nameValuePairs) {
        String[] nameValue = nameValuePair.split("=");
        try {
          map.put(URLDecoder.decode(nameValue[0], "UTF-8"), nameValue.length > 1 ? URLDecoder.decode(
                  nameValue[1], "UTF-8") : "");
        } catch (UnsupportedEncodingException e) {
          throw new RuntimeException("This method requires UTF-8 encoding support", e);
        }
      }
      return map;
    } catch (NumberFormatException e) {
      throw new PropertyException("Input " + input + " is not a valid double string");
    }
  }

  @Override
  public String toString(Map input) throws PropertyException {
    if (input==null)
      throw new PropertyException("Input cannot be null");
    StringBuilder stringBuilder = new StringBuilder();
    Map<String, String> map = input;
    for (String key : map.keySet()) {
      if (stringBuilder.length() > 0) {
        stringBuilder.append("&");
      }
      String value = map.get(key);
      try {
        stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
        stringBuilder.append("=");
        stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("This method requires UTF-8 encoding support", e);
      }
    }

    return stringBuilder.toString();
  }
}
