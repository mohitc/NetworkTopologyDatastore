package com.topology.impl.db.primitives;

import com.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import com.topology.primitives.*;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="network_element")
@NamedQueries({
    @NamedQuery(name=NetworkElementDBImpl.GET_CONTAINED_CONNECTION_POINTS_FOR_NETWORK_ELEMENT, query = "SELECT p FROM ConnectionPointDBImpl p WHERE p.parent = :parent AND p.topoManagerInstance = :instance"),
    @NamedQuery(name=NetworkElementDBImpl.GET_ALL_CONTAINED_CONNECTION_POINTS_FOR_NETWORK_ELEMENT, query = "SELECT p FROM ConnectionPointDBImpl p WHERE p.parentNe = :parent AND p.topoManagerInstance = :instance"),
    @NamedQuery(name=NetworkElementDBImpl.GET_CONNECTIONS_FOR_NETWORK_ELEMENT, query = "SELECT p FROM ConnectionDBImpl p WHERE (p.aEnd.parentNe = :ne OR p.zEnd.parentNe = :ne) AND p.topoManagerInstance = :instance"),
    @NamedQuery(name=NetworkElementDBImpl.GET_LINKS_FOR_NETWORK_ELEMENT, query = "SELECT p FROM LinkDBImpl p WHERE (p.aEnd.parentNe = :ne OR p.zEnd.parentNe = :ne) AND p.topoManagerInstance = :instance"),
    @NamedQuery(name=NetworkElementDBImpl.GET_CROSSCONNECTS_FOR_NETWORK_ELEMENT, query = "SELECT p FROM CrossConnectDBImpl p WHERE (p.aEnd.parentNe = :ne OR p.zEnd.parentNe = :ne) AND p.topoManagerInstance = :instance"),
    @NamedQuery(name=NetworkElementDBImpl.GET_CONNECTIONS_FOR_NETWORK_ELEMENT_BY_LAYER, query = "SELECT p FROM ConnectionDBImpl p WHERE (p.aEnd.parentNe = :ne OR p.zEnd.parentNe = :ne) AND (p.layer = :layer) AND p.topoManagerInstance = :instance"),
    @NamedQuery(name=NetworkElementDBImpl.GET_LINKS_FOR_NETWORK_ELEMENT_BY_LAYER, query = "SELECT p FROM LinkDBImpl p WHERE (p.aEnd.parentNe = :ne OR p.zEnd.parentNe = :ne) AND (p.layer = :layer) AND p.topoManagerInstance = :instance"),
    @NamedQuery(name=NetworkElementDBImpl.GET_CROSSCONNECTS_FOR_NETWORK_ELEMENT_BY_LAYER, query = "SELECT p FROM CrossConnectDBImpl p WHERE (p.aEnd.parentNe = :ne OR p.zEnd.parentNe = :ne) AND (p.layer = :layer) AND p.topoManagerInstance = :instance"),
})
public class NetworkElementDBImpl extends TopologyElementDBImpl implements NetworkElement {

  public static final String GET_CONTAINED_CONNECTION_POINTS_FOR_NETWORK_ELEMENT = "GET_CONTAINED_CONNECTION_POINTS_FOR_NETWORK_ELEMENT";

  public static final String GET_ALL_CONTAINED_CONNECTION_POINTS_FOR_NETWORK_ELEMENT = "GET_ALL_CONTAINED_CONNECTION_POINTS_FOR_NETWORK_ELEMENT";

  public static final String GET_CONNECTIONS_FOR_NETWORK_ELEMENT = "GET_CONNECTIONS_FOR_NETWORK_ELEMENT";

  public static final String GET_LINKS_FOR_NETWORK_ELEMENT = "GET_LINKS_FOR_NETWORK_ELEMENT";

  public static final String GET_CROSSCONNECTS_FOR_NETWORK_ELEMENT = "GET_CROSSCONNECTS_FOR_NETWORK_ELEMENT";

  public static final String GET_CONNECTIONS_FOR_NETWORK_ELEMENT_BY_LAYER = "GET_CONNECTIONS_FOR_NETWORK_ELEMENT_BY_LAYER";

  public static final String GET_LINKS_FOR_NETWORK_ELEMENT_BY_LAYER = "GET_LINKS_FOR_NETWORK_ELEMENT_BY_LAYER";

  public static final String GET_CROSSCONNECTS_FOR_NETWORK_ELEMENT_BY_LAYER = "GET_CROSSCONNECTS_FOR_NETWORK_ELEMENT_BY_LAYER";

  //default constructor
  public NetworkElementDBImpl(){
  }

  public NetworkElementDBImpl(TopologyManager manager) {
    super(manager);
  }

  private Set<ConnectionPoint> containedConnectionPoints (ConnectionPoint cp) {
    Set<ConnectionPoint> cps = new HashSet<>();
    cps.add(cp);
    if (Port.class.isAssignableFrom(cp.getClass())) {
      for (ConnectionPoint temp: ((Port)cp).getContainedConnectionPoints()) {
        cps.addAll(containedConnectionPoints(temp));
      }
    }
    return cps;
  }

  @Override
  public Set<ConnectionPoint> getConnectionPoints(boolean iterate) {
    EntityManager manager = EntityManagerFactoryHelper.getEntityManager();
    Query query;
    if (iterate)
      query = manager.createNamedQuery(NetworkElementDBImpl.GET_ALL_CONTAINED_CONNECTION_POINTS_FOR_NETWORK_ELEMENT);
    else
      query = manager.createNamedQuery(NetworkElementDBImpl.GET_CONTAINED_CONNECTION_POINTS_FOR_NETWORK_ELEMENT);
    query.setParameter("parent", this);
    query.setParameter("instance", this.topoManagerInstance);
    Set<ConnectionPoint> cpSet = new HashSet<>();
    cpSet.addAll(query.getResultList());
    return cpSet;
  }

  @Override
  public Set<Connection> getAllConnections() {
    EntityManager manager = EntityManagerFactoryHelper.getEntityManager();
    Query query = manager.createNamedQuery(NetworkElementDBImpl.GET_CONNECTIONS_FOR_NETWORK_ELEMENT);
    query.setParameter("ne", this);
    query.setParameter("instance", this.topoManagerInstance);
    Set<Connection> connSet = new HashSet<>();
    connSet.addAll(query.getResultList());
    return connSet;
  }

  @Override
  public Set<Connection> getAllConnections(NetworkLayer layer) {
    EntityManager manager = EntityManagerFactoryHelper.getEntityManager();
    Query query = manager.createNamedQuery(NetworkElementDBImpl.GET_CONNECTIONS_FOR_NETWORK_ELEMENT_BY_LAYER);
    query.setParameter("ne", this);
    query.setParameter("layer", layer);
    query.setParameter("instance", this.topoManagerInstance);
    Set<Connection> connSet = new HashSet<>();
    connSet.addAll(query.getResultList());
    return connSet;
  }

  @Override
  public <T extends Connection> Set<T> getAllConnections(Class<T> instance) {
    Set<T> connSet = new HashSet<>();
    if (instance==null)
      return connSet;
    EntityManager manager = EntityManagerFactoryHelper.getEntityManager();
    Query query;
    if (Link.class.isAssignableFrom(instance))
      query = manager.createNamedQuery(NetworkElementDBImpl.GET_LINKS_FOR_NETWORK_ELEMENT);
    else if (CrossConnect.class.isAssignableFrom(instance))
      query = manager.createNamedQuery(NetworkElementDBImpl.GET_CROSSCONNECTS_FOR_NETWORK_ELEMENT);
    else
      query = manager.createNamedQuery(NetworkElementDBImpl.GET_CONNECTIONS_FOR_NETWORK_ELEMENT);
    query.setParameter("ne", this);
    query.setParameter("instance", this.topoManagerInstance);
    connSet.addAll(query.getResultList());
    return connSet;
  }

  @Override
  public <T extends Connection> Set<T> getAllConnections(NetworkLayer layer, Class<T> instance) {
    Set<T> connSet = new HashSet<>();
    if (instance==null)
      return connSet;
    if (layer==null)
      return connSet;
    EntityManager manager = EntityManagerFactoryHelper.getEntityManager();
    Query query;
    if (Link.class.isAssignableFrom(instance))
      query = manager.createNamedQuery(NetworkElementDBImpl.GET_LINKS_FOR_NETWORK_ELEMENT_BY_LAYER);
    else if (CrossConnect.class.isAssignableFrom(instance))
      query = manager.createNamedQuery(NetworkElementDBImpl.GET_CROSSCONNECTS_FOR_NETWORK_ELEMENT_BY_LAYER);
    else
      query = manager.createNamedQuery(NetworkElementDBImpl.GET_CONNECTIONS_FOR_NETWORK_ELEMENT_BY_LAYER);
    query.setParameter("ne", this);
    query.setParameter("layer", layer);
    query.setParameter("instance", this.topoManagerInstance);
    connSet.addAll(query.getResultList());
    return connSet;
  }
}
