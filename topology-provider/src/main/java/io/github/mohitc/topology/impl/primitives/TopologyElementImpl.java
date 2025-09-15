package io.github.mohitc.topology.impl.primitives;

import io.github.mohitc.helpers.notification.annotation.PropChange;
import io.github.mohitc.topology.primitives.TopologyElement;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.exception.properties.PropertyException;
import io.github.mohitc.topology.primitives.properties.TEPropertyKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public abstract class TopologyElementImpl implements TopologyElement {

  private final TopologyManager manager;

  private int id;

  private String label;

  private final TEPropertyHelper propHelper;

  public TopologyElementImpl(TopologyManager manager, int id) {
    this.manager = manager;
    this.id = id;
    Logger log = LoggerFactory.getLogger(this.getClass());
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

  @Override
  public String getLabel() {
    return label;
  }

  @PropChange
  @Override
  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public Object getProperty(TEPropertyKey key)
    throws PropertyException {
    return propHelper.getProperty(key);
  }

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

  @Override
  public Set<TEPropertyKey> definedPropertyKeys() {
    return propHelper.definedPropertyKeys();
  }

  //Check if two instance of test.topology objects are equal
  //Check using ID
  @Override
  public boolean equals(Object o) {
    return  (this == o) ||
      ((o instanceof TopologyElement that) &&
      this.getID() == that.getID());
  }

  @Override
  public int hashCode() {
    // The hashCode contract requires that equal objects have the same hashCode.
    // Since equals() is based solely on the ID, the hashCode must also be based on the ID.
    return Integer.hashCode(id);
  }

  @Override
  public String toString() {
    return "(ID: " + this.getID() + ")";
  }
}
