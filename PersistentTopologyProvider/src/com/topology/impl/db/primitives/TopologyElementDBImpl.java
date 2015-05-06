package com.topology.impl.db.primitives;

import com.helpers.notification.annotation.PropChange;
import com.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import com.topology.primitives.TopologyElement;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.keys.TEPropertyKey;

import javax.persistence.Index;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.EntityManager;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.InheritanceType;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="topology_elements")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="jdoclass")
@NamedQueries({
  @NamedQuery(name=TopologyElementDBImpl.GET_TE_PROPERTY_BY_KEY, query = "Select t from TopologyElementDBImpl t JOIN t.teProperties q WHERE (t.id = :id AND q.key = :key and t.topoManagerInstance = :instance) "),
  @NamedQuery(name=TopologyElementDBImpl.GET_TE_BY_LABEL, query = "Select t from TopologyElementDBImpl t WHERE (t.label = :label AND t.topoManagerInstance = :instance)"),
  @NamedQuery(name=TopologyElementDBImpl.GET_NETWORK_ELEMENT_BY_LABEL, query = "Select t from NetworkElementDBImpl t WHERE (t.label = :label AND t.topoManagerInstance = :instance)"),
  @NamedQuery(name=TopologyElementDBImpl.GET_CP_BY_LABEL, query = "Select t from ConnectionPointDBImpl t WHERE (t.label = :label AND t.topoManagerInstance = :instance)"),
  @NamedQuery(name=TopologyElementDBImpl.GET_PORT_BY_LABEL, query = "Select t from PortDBImpl t WHERE (t.label = :label AND t.topoManagerInstance = :instance)"),
  @NamedQuery(name=TopologyElementDBImpl.GET_CONNECTION_BY_LABEL, query = "Select t from ConnectionDBImpl t WHERE (t.label = :label AND t.topoManagerInstance = :instance)"),
  @NamedQuery(name=TopologyElementDBImpl.GET_LINK_BY_LABEL, query = "Select t from LinkDBImpl t WHERE (t.label = :label AND t.topoManagerInstance = :instance)"),
  @NamedQuery(name=TopologyElementDBImpl.GET_CROSS_CONNECT_BY_LABEL, query = "Select t from CrossConnectDBImpl t WHERE (t.label = :label AND t.topoManagerInstance = :instance)")
})
abstract public class TopologyElementDBImpl implements TopologyElement {

  public static final String GET_TE_PROPERTY_BY_KEY = "GET_TE_PROPERTY_BY_KEY";

  public static final String GET_TE_BY_LABEL = "GET_TE_BY_LABEL";
  public static final String GET_NETWORK_ELEMENT_BY_LABEL = "GET_NETWORK_ELEMENT_BY_LABEL";
  public static final String GET_CP_BY_LABEL = "GET_CP_BY_LABEL";
  public static final String GET_PORT_BY_LABEL = "GET_PORT_BY_LABEL";
  public static final String GET_CONNECTION_BY_LABEL = "GET_CONNECTION_BY_LABEL";
  public static final String GET_LINK_BY_LABEL = "GET_LINK_BY_LABEL";
  public static final String GET_CROSS_CONNECT_BY_LABEL = "GET_CROSS_CONNECT_BY_LABEL";

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy=GenerationType.AUTO)
  private int id;

  @Column(name = "manager", nullable = false)
  protected String topoManagerInstance;

  @Column(name = "label")
  private String label;

  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
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
  @PropChange
  public void setLabel(String label) {
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    em.getTransaction().begin();
    TopologyElementDBImpl te = em.find(TopologyElementDBImpl.class, this.getID());
    te.setLabelString(label);
    this.label = label;
    em.getTransaction().commit();
    em.close();
  }

  private void setLabelString (String label) {
    this.label = label;
  }

  @Override
  public Object getProperty(TEPropertyKey key) throws PropertyException {
    if (key==null)
      throw new PropertyException("Property key cannot be null");
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    //Query query = em.createQuery(TopologyElementDBImpl.GET_TE_PROPERTY_BY_KEY);
    //query.setParameter("id", id);
    //query.setParameter("key", key);

    //TEPropertyDBImpl prop = (TEPropertyDBImpl)query.getSingleResult();
    if (teProperties!=null)
      for (TEPropertyDBImpl prop: teProperties) {
        if (prop.getKey().equals(key))
          return prop;
      }
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
  @PropChange
  public void addProperty(TEPropertyKey key, Object value) throws PropertyException {
    if (key==null)
      throw new PropertyException("Property key cannot be null");
    if (value==null)
      throw new PropertyException("Value cannot be null");
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    em.getTransaction().begin();
    TopologyElementDBImpl te = em.find(TopologyElementDBImpl.class, this.getID());

    //Query query = em.createQuery(TopologyElementDBImpl.GET_TE_PROPERTY_BY_KEY);
    //query.setParameter("id", id);
    //query.setParameter("key", key);

//    TEPropertyDBImpl prop = (TEPropertyDBImpl)query.getSingleResult();
    TEPropertyDBImpl prop = null;
    if (te.teProperties!=null)
      for (TEPropertyDBImpl temp: te.teProperties) {
        if (temp.getKey().equals(key)) {
          prop = temp;
          break;
        }
      }

    if (prop==null) {
      prop = new TEPropertyDBImpl();
      prop.setKey(key);
      prop.setValue(value.toString());
      te.teProperties.add(prop);
      this.teProperties.add(prop);
      em.persist(prop);
    } else {
      prop.setValue(value.toString());
    }
    em.getTransaction().commit();
  }

  @Override
  @PropChange
  public void removeProperty(TEPropertyKey key) throws PropertyException {
    if (key==null)
      throw new PropertyException("Property key cannot be null");

//    Query query = em.createQuery(TopologyElementDBImpl.GET_TE_PROPERTY_BY_KEY);
//    query.setParameter("id", id);
//    query.setParameter("key", key);

//    TEPropertyDBImpl prop = (TEPropertyDBImpl)query.getSingleResult();

    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    em.getTransaction().begin();
    TopologyElementDBImpl te = em.find(TopologyElementDBImpl.class, this.getID());

    //Query query = em.createQuery(TopologyElementDBImpl.GET_TE_PROPERTY_BY_KEY);
    //query.setParameter("id", id);
    //query.setParameter("key", key);

//    TEPropertyDBImpl prop = (TEPropertyDBImpl)query.getSingleResult();
    TEPropertyDBImpl prop = null;
    if (te.teProperties!=null)
      for (TEPropertyDBImpl temp: te.teProperties) {
        if (temp.getKey().equals(key)) {
          prop = temp;
          break;
        }
      }

    if (prop==null) {
      throw new PropertyException("Cannot remove a property that does not exist");
    }
    em.remove(prop);
    em.getTransaction().commit();
    em.close();
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
