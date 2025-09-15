package io.github.mohitc.topology.impl.db.primitives;

import io.github.mohitc.topology.primitives.*;
import io.github.mohitc.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    // Empty constructor to support initialization by persistence.
  }

  public NetworkElementDBImpl(TopologyManager manager) {
    super(manager);
  }

  @Override
  public Set<ConnectionPoint> getConnectionPoints(boolean iterate) {
    try (EntityManager manager = EntityManagerFactoryHelper.getEntityManager()) {
      TypedQuery<ConnectionPointDBImpl> query;
      if (iterate) {
        query = manager.createNamedQuery(GET_ALL_CONTAINED_CONNECTION_POINTS_FOR_NETWORK_ELEMENT, ConnectionPointDBImpl.class);
      } else {
        query = manager.createNamedQuery(GET_CONTAINED_CONNECTION_POINTS_FOR_NETWORK_ELEMENT, ConnectionPointDBImpl.class);
      }
      query.setParameter("parent", this);
      query.setParameter("instance", this.topoManagerInstance);
      return query.getResultStream().collect(Collectors.toSet());
    }
  }

  @Override
  public Set<Connection> getAllConnections() {
    try (EntityManager manager = EntityManagerFactoryHelper.getEntityManager()) {
      TypedQuery<ConnectionDBImpl> query = manager.createNamedQuery(GET_CONNECTIONS_FOR_NETWORK_ELEMENT, ConnectionDBImpl.class);
      query.setParameter("ne", this);
      query.setParameter("instance", this.topoManagerInstance);
      return query.getResultStream().collect(Collectors.toSet());
    }
  }

  @Override
  public Set<Connection> getAllConnections(NetworkLayer layer) {
    try(EntityManager manager = EntityManagerFactoryHelper.getEntityManager()) {
      TypedQuery<ConnectionDBImpl> query = manager.createNamedQuery(GET_CONNECTIONS_FOR_NETWORK_ELEMENT_BY_LAYER, ConnectionDBImpl.class);
      query.setParameter("ne", this);
      query.setParameter("layer", layer);
      query.setParameter("instance", this.topoManagerInstance);
      return query.getResultStream().collect(Collectors.toSet());
    }
  }

  @Override
  public <T extends Connection> Set<T> getAllConnections(Class<T> instance) {
    Set<T> connSet = new HashSet<>();
    if (instance==null) {
      return connSet;
    }
    try (EntityManager manager = EntityManagerFactoryHelper.getEntityManager()) {
      TypedQuery<? extends Connection> query;
      if (Link.class.isAssignableFrom(instance)) {
        query = manager.createNamedQuery(GET_LINKS_FOR_NETWORK_ELEMENT, LinkDBImpl.class);
      } else if (CrossConnect.class.isAssignableFrom(instance)) {
        query = manager.createNamedQuery(GET_CROSSCONNECTS_FOR_NETWORK_ELEMENT, CrossConnectDBImpl.class);
      } else {
        query = manager.createNamedQuery(GET_CONNECTIONS_FOR_NETWORK_ELEMENT, ConnectionDBImpl.class);
      }
      query.setParameter("ne", this);
      query.setParameter("instance", this.topoManagerInstance);
      query.getResultStream().forEach(v -> connSet.add((T) v));
      return connSet;
    }
  }

  @Override
  public <T extends Connection> Set<T> getAllConnections(NetworkLayer layer, Class<T> instance) {
    Set<T> connSet = new HashSet<>();
    if (instance==null) {
      return connSet;
    }
    if (layer==null) {
      return connSet;
    }
    try (EntityManager manager = EntityManagerFactoryHelper.getEntityManager()) {
      TypedQuery<? extends ConnectionDBImpl> query;
      if (Link.class.isAssignableFrom(instance)) {
        query = manager.createNamedQuery(GET_LINKS_FOR_NETWORK_ELEMENT_BY_LAYER, LinkDBImpl.class);
      } else if (CrossConnect.class.isAssignableFrom(instance)) {
        query = manager.createNamedQuery(GET_CROSSCONNECTS_FOR_NETWORK_ELEMENT_BY_LAYER, CrossConnectDBImpl.class);
      } else {
        query = manager.createNamedQuery(GET_CONNECTIONS_FOR_NETWORK_ELEMENT_BY_LAYER, ConnectionDBImpl.class);
      }
      query.setParameter("ne", this);
      query.setParameter("layer", layer);
      query.setParameter("instance", this.topoManagerInstance);
      query.getResultStream().forEach(v -> connSet.add((T) v));
      return connSet;
    }
  }
}
