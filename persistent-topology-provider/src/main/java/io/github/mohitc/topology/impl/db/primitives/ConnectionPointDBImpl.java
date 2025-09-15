package io.github.mohitc.topology.impl.db.primitives;


import io.github.mohitc.topology.primitives.*;
import io.github.mohitc.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="connection_point")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
  @NamedQuery(name=ConnectionPointDBImpl.GET_CONNECTIONS_FOR_CONNECTION_POINT, query = "SELECT p FROM ConnectionDBImpl p WHERE (p.aEnd = :cp OR p.zEnd = :cp) AND p.topoManagerInstance = :instance"),
  @NamedQuery(name=ConnectionPointDBImpl.GET_LINKS_FOR_CONNECTION_POINT, query = "SELECT p FROM LinkDBImpl p WHERE (p.aEnd = :cp OR p.zEnd = :cp) AND p.topoManagerInstance = :instance"),
  @NamedQuery(name=ConnectionPointDBImpl.GET_CROSSCONNECTS_FOR_CONNECTION_POINT, query = "SELECT p FROM CrossConnectDBImpl p WHERE (p.aEnd = :cp OR p.zEnd = :cp) AND p.topoManagerInstance = :instance"),
  @NamedQuery(name=ConnectionPointDBImpl.GET_CONNECTIONS_FOR_CONNECTION_POINT_BY_LAYER, query = "SELECT p FROM ConnectionDBImpl p WHERE (p.aEnd = :cp OR p.zEnd = :cp) AND p.layer = :layer AND p.topoManagerInstance = :instance"),
  @NamedQuery(name=ConnectionPointDBImpl.GET_LINKS_FOR_CONNECTION_POINT_BY_LAYER, query = "SELECT p FROM LinkDBImpl p WHERE (p.aEnd = :cp OR p.zEnd = :cp) AND p.layer = :layer AND p.topoManagerInstance = :instance"),
  @NamedQuery(name=ConnectionPointDBImpl.GET_CROSSCONNECTS_FOR_CONNECTION_POINT_BY_LAYER, query = "SELECT p FROM CrossConnectDBImpl p WHERE (p.aEnd = :cp OR p.zEnd = :cp) AND p.layer = :layer AND p.topoManagerInstance = :instance")
})
public class ConnectionPointDBImpl extends TopologyElementDBImpl implements ConnectionPoint {

  public static final String GET_CONNECTIONS_FOR_CONNECTION_POINT = "GET_CONNECTIONS_FOR_CONNECTION_POINT";

  public static final String GET_CONNECTIONS_FOR_CONNECTION_POINT_BY_LAYER = "GET_CONNECTIONS_FOR_CONNECTION_POINT_BY_LAYER";

  public static final String GET_LINKS_FOR_CONNECTION_POINT = "GET_LINKS_FOR_CONNECTION_POINT";

  public static final String GET_CROSSCONNECTS_FOR_CONNECTION_POINT = "GET_CROSSCONNECTS_FOR_CONNECTION_POINT";

  public static final String GET_LINKS_FOR_CONNECTION_POINT_BY_LAYER = "GET_LINKS_FOR_CONNECTION_POINT_BY_LAYER";

  public static final String GET_CROSSCONNECTS_FOR_CONNECTION_POINT_BY_LAYER = "GET_CROSSCONNECTS_FOR_CONNECTION_POINT_BY_LAYER";

  @OneToOne
  @JoinColumn(name="parent_ne_id", referencedColumnName = "id")
  private NetworkElementDBImpl parentNe;
  private TypedQuery<ConnectionDBImpl> query;

  public ConnectionPointDBImpl() {
    // Empty constructor to support initialization by persistence.
  }

  public ConnectionPointDBImpl(TopologyManager manager, TopologyElementDBImpl parent) {
    super(manager);
    this.parent = parent;
    if (parent !=null) {
      if (PortDBImpl.class.isAssignableFrom(parent.getClass())) {
        parentNe = ((ConnectionPointDBImpl)parent).parentNe;
      } else if (NetworkElementDBImpl.class.isAssignableFrom(parent.getClass())){
        parentNe = (NetworkElementDBImpl)parent;
      }
    }
  }

  @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name="parent", referencedColumnName = "id")
  private TopologyElementDBImpl parent;

  @Override
  public Set<Connection> getConnections() {
    try (EntityManager manager = EntityManagerFactoryHelper.getEntityManager()) {
      TypedQuery<ConnectionDBImpl> query = manager.createNamedQuery(GET_CONNECTIONS_FOR_CONNECTION_POINT, ConnectionDBImpl.class);
      query.setParameter("cp", this);
      query.setParameter("instance", this.topoManagerInstance);
      return query.getResultStream().collect(Collectors.toSet());
    }
  }

  @Override
  public <T extends Connection> Set<T> getConnections(Class<T> instance) {
    Set<T> connSet = new HashSet<>();
    if (instance==null) {
      return connSet;
    }
    try (EntityManager manager = EntityManagerFactoryHelper.getEntityManager()) {
      TypedQuery<? extends ConnectionDBImpl> query;
      if (Link.class.isAssignableFrom(instance)) {
        query = manager.createNamedQuery(GET_LINKS_FOR_CONNECTION_POINT, LinkDBImpl.class);
      } else if (CrossConnect.class.isAssignableFrom(instance)) {
        query = manager.createNamedQuery(GET_CROSSCONNECTS_FOR_CONNECTION_POINT, CrossConnectDBImpl.class);
      } else {
        query = manager.createNamedQuery(GET_CONNECTIONS_FOR_CONNECTION_POINT, ConnectionDBImpl.class);
      }
      query.setParameter("cp", this);
      query.setParameter("instance", this.topoManagerInstance);
      query.getResultStream().forEach(v -> connSet.add((T) v));
      return connSet;
    }
  }

  @Override
  public Set<Connection> getConnections(NetworkLayer layer) {
    Set<Connection> connSet = new HashSet<>();
    if (layer==null) {
      return connSet;
    }
    try (EntityManager manager = EntityManagerFactoryHelper.getEntityManager()) {
      TypedQuery<ConnectionDBImpl> query = manager.createNamedQuery(GET_CONNECTIONS_FOR_CONNECTION_POINT_BY_LAYER, ConnectionDBImpl.class);
      query.setParameter("cp", this);
      query.setParameter("layer", layer);
      query.setParameter("instance", this.topoManagerInstance);
      return query.getResultStream().collect(Collectors.toSet());
    }
  }

  @Override
  public <T extends Connection> Set<T> getConnections(NetworkLayer layer, Class<T> instance) {
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
        query = manager.createNamedQuery(GET_LINKS_FOR_CONNECTION_POINT_BY_LAYER, LinkDBImpl.class);
      } else if (CrossConnect.class.isAssignableFrom(instance)) {
        query = manager.createNamedQuery(GET_CROSSCONNECTS_FOR_CONNECTION_POINT_BY_LAYER, CrossConnectDBImpl.class);
      } else {
        query = manager.createNamedQuery(GET_CONNECTIONS_FOR_CONNECTION_POINT_BY_LAYER, ConnectionDBImpl.class);
      }
      query.setParameter("cp", this);
      query.setParameter("layer", layer);
      query.setParameter("instance", this.topoManagerInstance);
      query.getResultStream().forEach(v -> connSet.add((T) v));
      return connSet;
    }
  }

  @Override
  public TopologyElement getParent() {
    return parent;
  }
}
