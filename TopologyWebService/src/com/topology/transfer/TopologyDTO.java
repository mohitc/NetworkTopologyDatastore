package com.topology.transfer;

import com.topology.primitives.TopologyElement;
import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.keys.TEPropertyKey;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class TopologyDTO {

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
        for(TEPropertyKey key: keySet) {
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
    for (T te: tes) {
      idSet.add(te.getID());
    }
    return idSet;
  }
}
