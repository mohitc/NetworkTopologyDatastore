package com.topology.primitives.properties.keys;

import com.topology.primitives.exception.properties.PropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum TEPropertyKey {
  XCOORD(1, "XCoord", "X coordinate of network element", Double.class),
  YCOORD(2, "YCoord", "Y coordinate of network element", Double.class);

  private static final Logger log = LoggerFactory.getLogger(TEPropertyKey.class);

  TEPropertyKey (int value, String label, String desc, Class objClass) {
    this.value = value;
    this.desc = desc;
    this.label = label;
    this.objClass = objClass;
  }

  private int value;
  private String desc, label;
  private Class objClass;

  public int getIntValue() {
    return value;
  }

  public String getDescription() {
    return desc;
  }

  public String getLabel() {
    return label;
  }

  public Class getObjectClass() {
    return objClass;
  }

  public <T extends TEPropertyKey> T getKeyFromValue(int x) throws PropertyException {
    return null;
  }

  public static Object getObjectForKey(TEPropertyKey key, String val) throws PropertyException {
    if (key==null) {
      throw new PropertyException("Key cannot be null");
    }
    if (val==null) {
      throw new PropertyException("Value cannot be null");
    }
    if (Double.class.isAssignableFrom(key.getObjectClass())) {
      try {
        return Double.parseDouble(val);
      } catch (NumberFormatException e) {
        throw new PropertyException("Invalid number format when parsing string : " + val + " as a double");
      }
    }
    throw new PropertyException("Parser not defined to parse object of class: " + key.getObjectClass() + " in TE Properties");
  }
}
