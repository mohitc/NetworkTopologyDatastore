package com.topology.impl.db.primitives.converters;


import com.topology.primitives.NetworkLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class NetworkLayerConverter implements AttributeConverter<NetworkLayer, String>{

  private static final Logger log = LoggerFactory.getLogger(NetworkLayerConverter.class);

  @Override
  public String convertToDatabaseColumn(NetworkLayer networkLayer) {
    if (networkLayer!=null)
      return networkLayer.toString();
    else
      return "";
  }

  @Override
  public NetworkLayer convertToEntityAttribute(String s) {
    if ((s==null)||(s.equals("")))
        return null;
    try {
      return NetworkLayer.valueOf(s);
    } catch (IllegalArgumentException e) {
      log.error("Illegal string argument ("+ s + ") attempted to be parsed as Network Layer", e);
      return null;
    }
  }
}
