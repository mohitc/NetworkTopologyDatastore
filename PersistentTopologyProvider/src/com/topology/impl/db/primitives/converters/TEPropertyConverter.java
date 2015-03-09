package com.topology.impl.db.primitives.converters;

import com.topology.primitives.properties.keys.TEPropertyKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TEPropertyConverter implements AttributeConverter<TEPropertyKey, String> {

  private static final Logger log = LoggerFactory.getLogger(TEPropertyConverter.class);

  @Override
  public String convertToDatabaseColumn(TEPropertyKey tePropertyKey) {
    if (tePropertyKey!=null)
      return tePropertyKey.toString();
    else
      return "";
  }

  @Override
  public TEPropertyKey convertToEntityAttribute(String s) {
    if ((s==null)||(s.equals("")))
      return null;
    try {
      return TEPropertyKey.valueOf(s);
    } catch (IllegalArgumentException e) {
      log.error("Illegal string argument ("+ s + ") attempted to be parsed as TE Property Key", e);
      return null;
    }
  }
}
