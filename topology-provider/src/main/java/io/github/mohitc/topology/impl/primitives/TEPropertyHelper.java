package io.github.mohitc.topology.impl.primitives;

import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.exception.properties.PropertyException;
import io.github.mohitc.topology.primitives.properties.TEPropertyKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TEPropertyHelper {

  private final Logger log;

  private final TopologyManager manager;

  private final Map<TEPropertyKey, String> properties;

  //should only be instantiated by objects in this package
  protected TEPropertyHelper(TopologyManager instance, Logger log) {
    this.manager = instance;
    if (log!=null) {
      this.log = log;
    } else {
      this.log = LoggerFactory.getLogger(TEPropertyHelper.class);
    }
    properties = new HashMap<>();
  }

  public Object getProperty(TEPropertyKey key)
      throws PropertyException {
    if (key==null) {
      throw new PropertyException("Key cannot be null");
    }
    if (!hasProperty(key)) {
      throw new PropertyException("Property not found");
    }
    if (!manager.containsKey(key)) {
      throw new PropertyException("Key not registered with the TopologyManager");
    }
    String out = properties.get(key);
    return key.getConverter().fromString(out);
  }

  @SuppressWarnings("unchecked")
  public <K> K getProperty(TEPropertyKey key, Class<K> instance)
      throws PropertyException {
    Object out = getProperty(key);
    if (out==null) {
      throw new PropertyException("Value is null");
    }
    if (instance.isAssignableFrom(out.getClass())) {
      return (K)out;
    } else {
      throw new PropertyException("The value for key " + key + " is of type " + out.getClass() + " which is not assignable from " + instance);
    }
  }

  public boolean hasProperty(TEPropertyKey key) {
    try {
      return (key!=null) && properties.containsKey(key) && manager.containsKey(key);
    } catch (Exception e) {
      log.error("Unexpected exception when checking for property", e);
      return false;
    }
  }

  public void addProperty(TEPropertyKey key, Object value)
      throws PropertyException {
    if (key == null) {
      throw new PropertyException("Key cannot be null");
    }
    if (value == null) {
      throw new PropertyException("Value cannot be null");
    }
    if (!manager.containsKey(key)) {
      throw new PropertyException("Key not registered with the TopologyManager");
    }
    try {
      properties.put(key, key.getConverter().toString(value));
    } catch (Exception e) {
      log.error("Error when adding property to map", e);
      throw new PropertyException(e);
    }
  }

  public void removeProperty(TEPropertyKey key)
      throws PropertyException {
    if (key == null) {
      throw new PropertyException("Key cannot be null");
    }
    if (!properties.containsKey(key)) {
      throw new PropertyException("Property not set for test.topology element");
    }
    try {
      properties.remove(key);
    } catch (Exception e) {
      log.error("Error when removing property from map", e);
      throw new PropertyException(e);
    }
  }

  public Set<TEPropertyKey> definedPropertyKeys() {
    return properties.keySet();
  }

}
