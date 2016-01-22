package com.topology.impl.primitives;

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

	private TEPropertyHelper propHelper;

	private static final Logger log = LoggerFactory.getLogger(TopologyElement.class);

	public TopologyElementImpl(TopologyManager manager, int id) {
		this.manager = manager;
    this.id = id;
    propHelper = new TEPropertyHelper(manager, log);
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
    return propHelper.getProperty(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getProperty(TEPropertyKey key, Class<K> instance)
			throws PropertyException {
    return propHelper.getProperty(key, instance);
	}

	@Override
	public boolean hasProperty(TEPropertyKey key) {
    return propHelper.hasProperty(key);
	}

	@Override
  @PropChange
  public void addProperty(TEPropertyKey key, Object value)
			throws PropertyException {
    propHelper.addProperty(key, value);
	}

	@Override
  @PropChange
  public void removeProperty(TEPropertyKey key)
			throws PropertyException {
    propHelper.removeProperty(key);
	}

    public Set<TEPropertyKey> definedPropertyKeys() {
      return propHelper.definedPropertyKeys();
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
