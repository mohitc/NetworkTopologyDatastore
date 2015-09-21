package com.topology.impl.primitives;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.helpers.notification.annotation.PropChange;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.properties.TEPropertyKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topology.primitives.TopologyElement;
import com.topology.primitives.exception.properties.PropertyException;

public abstract class TopologyElementImpl implements TopologyElement {

  private TopologyManager manager;

	private int id;

	private String label;

	private Map<TEPropertyKey, String> properties;

	private static final Logger log = LoggerFactory.getLogger(TopologyElement.class);

	public TopologyElementImpl(TopologyManager manager, int id) {
		this.manager = manager;
    this.id = id;
		properties = new HashMap<>();
	}

  @Override
  public TopologyManager getTopologyManager() {
    return manager;
  }

  @Override
	public int getID() {
		return id;
	}

	protected void setID(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

  @PropChange
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public Object getProperty(TEPropertyKey key)
			throws PropertyException {
		if (key==null)
				throw new PropertyException("Key cannot be null");
		if (!hasProperty(key))
			throw new PropertyException("Property not found");
    if (!manager.containsKey(key))
      throw new PropertyException("Key not registered with the TopologyManager");
		String out = properties.get(key);
    return key.getConverter().fromString(out);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getProperty(TEPropertyKey key, Class<K> instance)
			throws PropertyException {
		Object out = getProperty(key);
		if (out==null) throw new PropertyException("Value is null");
		if (instance.isAssignableFrom(out.getClass())) {
			return (K)out;
		} else throw new PropertyException("The value for key " + key + " is of type " + out.getClass() + " which is not assignable from " + instance);
	}

	@Override
	public boolean hasProperty(TEPropertyKey key) {
		try {
			return (key!=null) && properties.containsKey(key) && manager.containsKey(key);
		} catch (Exception e) {
			log.error("Unexpected exception when checking for property", e);
			return false;
		}
	}

	@Override
  @PropChange
  public void addProperty(TEPropertyKey key, Object value)
			throws PropertyException {
		if (key == null) 
			throw new PropertyException("Key cannot be null");
		if (value == null) 
			throw new PropertyException("Value cannot be null");
    if (!manager.containsKey(key))
      throw new PropertyException("Key not registered with the TopologyManager");
		try { 
			properties.put(key, key.getConverter().toString(value));
		} catch (Exception e) {
			log.error("Error when adding property to map", e);
			throw new PropertyException(e.getMessage());
		}
	}

	@Override
  @PropChange
  public void removeProperty(TEPropertyKey key)
			throws PropertyException {
		if (key == null) 
			throw new PropertyException("Key cannot be null");
		if (!properties.containsKey(key)) 
			throw new PropertyException("Property not set for test.topology element");
		try { 
			properties.remove(key);
		} catch (Exception e) {
			log.error("Error when removing property from map", e);
			throw new PropertyException(e.getMessage());
		}
	}

    public Set<TEPropertyKey> definedPropertyKeys() {
        return properties.keySet();
    }

    //Check if two instance of test.topology objects are equal
	//Check using ID
	public boolean equals (Object o) {
		if (o!=null) {
			if (this.getClass().isAssignableFrom(o.getClass())) {
				if (((TopologyElement)o).getID()==this.id) {
					return true;
				}
			}
		}
		return false;
	}

    public String toString() {
        return "(ID: " + String.valueOf(this.getID()) + ")";
    }
}
