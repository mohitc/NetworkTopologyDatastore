package com.topology.dto;

import com.topology.primitives.*;
import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.TEPropertyKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class TopologyDTO {

  private static final Logger log = LoggerFactory.getLogger(TopologyDTO.class);

  private int id;

  private String label;

  private Map<TEPropertyKey, Object> properties;

  public TopologyDTO() {
    //Default constructor for serialization functions
  }

  public <T extends TopologyElement> void populateDTO(T te) throws PropertyException {
    this.setId(te.getID());
    this.setLabel(te.getLabel());
    this.properties = new HashMap<>();
    Set<TEPropertyKey> keySet = te.definedPropertyKeys();
    for (TEPropertyKey key : keySet) {
      properties.put(key, te.getProperty(key));
    }
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Map<TEPropertyKey, Object> getProperties() {
    return properties;
  }

  public void setProperties(Map<TEPropertyKey, Object> properties) {
    this.properties = properties;
  }

  protected static <T extends TopologyElement> Set<Integer> generateSet(Set<T> tes) {
    Set<Integer> idSet = new HashSet<>();
    if (tes == null)
      return idSet;
    for (T te : tes) {
      idSet.add(te.getID());
    }
    return idSet;
  }

  public static TopologyDTO generateDTO (TopologyElement te) throws PropertyException {
    if (te==null) {
      log.debug("Topology element is null");
      return null;
    }

    TopologyDTO dto;
    if (NetworkElement.class.isAssignableFrom(te.getClass())) {
      dto = new NetworkElementDTO((NetworkElement)te);
    } else if (ConnectionPoint.class.isAssignableFrom(te.getClass())) {
      if (Port.class.isAssignableFrom(te.getClass())) {
        dto = new PortDTO((Port)te);
      } else {
        dto = new ConnectionPointDTO((ConnectionPoint)te);
      }
    } else if (Connection.class.isAssignableFrom(te.getClass())) {
      if (Link.class.isAssignableFrom(te.getClass())) {
        dto = new LinkDTO((Link)te);
      } else if (CrossConnect.class.isAssignableFrom(te.getClass())){
        dto = new CrossConnectDTO((CrossConnect)te);
      } else {
        throw new PropertyException("DTO generation not defined for Connection Class: " + te.getClass().getSimpleName());
      }
    } else {
      throw new PropertyException("DTO generation not defined for Topology Element Class: " + te.getClass().getSimpleName());
    }
//    dto.populateDTO(te);
    return dto;
  }

  public String toString() {
    String out = "Topology Element: " + this.getClass().getSimpleName() + ", ID: " + this.getId() + ", label: " + this.getLabel();

    Set<TEPropertyKey> propertyKeys = (properties!=null) ? properties.keySet(): null;
    if ((propertyKeys!=null) && (propertyKeys.size()>0)) {
      out = out + ", Properties: {";
      for (TEPropertyKey key : propertyKeys) {
        out = out + key.getId() + ", ";
      }
      out = out.substring(0, out.length() - 2) + "}";
    }
    return out;
  }
}
