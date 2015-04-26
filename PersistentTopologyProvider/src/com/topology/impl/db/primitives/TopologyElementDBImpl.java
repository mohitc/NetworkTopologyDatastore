package com.topology.impl.db.primitives;

import com.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import com.topology.primitives.TopologyElement;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.keys.TEPropertyKey;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name="topology_elements")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="jdoclass")
@NamedQueries({
    @NamedQuery(name=TopologyElementDBImpl.GET_TE_PROPERTY_BY_KEY, query = "Select t from TopologyElementDBImpl t JOIN t.teProperties q WHERE (t.id = :id AND q.key = :key)")
})
abstract public class TopologyElementDBImpl implements TopologyElement {

  public static final String GET_TE_PROPERTY_BY_KEY = "GET_TE_PROPERTY_BY_KEY";

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy=GenerationType.AUTO)
  private int id;

  @Column(name = "manager", nullable = false)
  private String topoManagerInstance;

  @Column(name = "label")
  private String label;

  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name="te_id")
  private Set<TEPropertyDBImpl> teProperties;

  //Default constructor
  public TopologyElementDBImpl() {
  }

  //constructor with the topology manager instance assigned
  public TopologyElementDBImpl(TopologyManager manager) {
    this.topoManagerInstance = manager.getIdentifier();
  }

  @Override
  public TopologyManager getTopologyManager() {
    return new TopologyManagerDBImpl(topoManagerInstance);
  }

  @Override
  public int getID() {
    return id;
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public void setLabel(String label) {
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    em.getTransaction().begin();
    this.label = label;
    em.getTransaction().commit();
    em.close();
  }

  @Override
  public Object getProperty(TEPropertyKey key) throws PropertyException {
    if (key==null)
      throw new PropertyException("Property key cannot be null");
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    Query query = em.createQuery(TopologyElementDBImpl.GET_TE_PROPERTY_BY_KEY);
    query.setParameter("id", id);
    query.setParameter("key", key);

    TEPropertyDBImpl prop = (TEPropertyDBImpl)query.getSingleResult();
    if (prop!=null)
      return TEPropertyKey.getObjectForKey(key, prop.getValue());
    else
      throw new PropertyException("Key not found in topology element");
  }

  @Override
  public <K> K getProperty(TEPropertyKey key, Class<K> instance) throws PropertyException {
    Object o = getProperty(key);
    if (o.getClass().isAssignableFrom(instance))
       return (K)o;
    else
      throw new PropertyException("Property value of type :" + o.getClass() + " cannot be cast to type " + instance);
  }

  @Override
  public boolean hasProperty(TEPropertyKey key) {
    try {
      Object o = getProperty(key);
      return true;
    } catch (PropertyException e) {
      //property not found in the set of properties
      return false;
    }
  }

  @Override
  public void addProperty(TEPropertyKey key, Object value) throws PropertyException {
    if (key==null)
      throw new PropertyException("Property key cannot be null");
    if (value==null)
      throw new PropertyException("Value cannot be null");
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    em.getTransaction().begin();

    Query query = em.createQuery(TopologyElementDBImpl.GET_TE_PROPERTY_BY_KEY);
    query.setParameter("id", id);
    query.setParameter("key", key);

    TEPropertyDBImpl prop = (TEPropertyDBImpl)query.getSingleResult();
    if (prop==null) {
      prop = new TEPropertyDBImpl();
      prop.setKey(key);
    }
    prop.setValue(value.toString());
    em.getTransaction().commit();

  }

  @Override
  public void removeProperty(TEPropertyKey key) throws PropertyException {
    if (key==null)
      throw new PropertyException("Property key cannot be null");
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();

    Query query = em.createQuery(TopologyElementDBImpl.GET_TE_PROPERTY_BY_KEY);
    query.setParameter("id", id);
    query.setParameter("key", key);

    TEPropertyDBImpl prop = (TEPropertyDBImpl)query.getSingleResult();
    if (prop==null) {
      throw new PropertyException("Cannot remove a property that does not exist");
    }

    em.getTransaction().begin();
    this.teProperties.remove(prop);
    em.getTransaction().commit();
  }

  @Override
  public Set<TEPropertyKey> definedPropertyKeys() {
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    Set<TEPropertyKey> keys = new HashSet<>();
    for (TEPropertyDBImpl propDB: teProperties) {
      keys.add(propDB.getKey());
    }
    return keys;
  }

  public boolean equals(Object o) {
    if (o!=null) {
      if (this.getClass().isAssignableFrom(o.getClass())) {
        if (((TopologyElement)o).getID()==this.id) {
          return true;
        }
      }
    }
    return false;
  }

  public int hashCode() {
    return new Integer(this.getID()).hashCode();
  }

  public String toString() {
    return "(ID: " + String.valueOf(this.getID()) + ")";
  }

}
