package com.topology.impl.db.primitives;


import com.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import com.topology.primitives.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="connection_point")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
    @NamedQuery(name=ConnectionPointDBImpl.GET_CONNECTIONS_FOR_CONNECTION_POINT, query = "SELECT p FROM ConnectionDBImpl p WHERE (p.aEnd = :cp OR p.zEnd = :cp)"),
    @NamedQuery(name=ConnectionPointDBImpl.GET_LINKS_FOR_CONNECTION_POINT, query = "SELECT p FROM LinkDBImpl p WHERE (p.aEnd = :cp OR p.zEnd = :cp)"),
    @NamedQuery(name=ConnectionPointDBImpl.GET_CROSSCONNECTS_FOR_CONNECTION_POINT, query = "SELECT p FROM CrossConnectDBImpl p WHERE (p.aEnd = :cp OR p.zEnd = :cp)"),
    @NamedQuery(name=ConnectionPointDBImpl.GET_CONNECTIONS_FOR_CONNECTION_POINT_BY_LAYER, query = "SELECT p FROM ConnectionDBImpl p WHERE (p.aEnd = :cp OR p.zEnd = :cp) AND p.layer = :layer"),
    @NamedQuery(name=ConnectionPointDBImpl.GET_LINKS_FOR_CONNECTION_POINT_BY_LAYER, query = "SELECT p FROM LinkDBImpl p WHERE (p.aEnd = :cp OR p.zEnd = :cp) AND p.layer = :layer"),
    @NamedQuery(name=ConnectionPointDBImpl.GET_CROSSCONNECTS_FOR_CONNECTION_POINT_BY_LAYER, query = "SELECT p FROM CrossConnectDBImpl p WHERE (p.aEnd = :cp OR p.zEnd = :cp) AND p.layer = :layer")
})
public class ConnectionPointDBImpl extends TopologyElementDBImpl implements ConnectionPoint {

  public static final String GET_CONNECTIONS_FOR_CONNECTION_POINT = "GET_CONNECTIONS_FOR_CONNECTION_POINT";

  public static final String GET_CONNECTIONS_FOR_CONNECTION_POINT_BY_LAYER = "GET_CONNECTIONS_FOR_CONNECTION_POINT_BY_LAYER";

  public static final String GET_LINKS_FOR_CONNECTION_POINT = "GET_LINKS_FOR_CONNECTION_POINT";

  public static final String GET_CROSSCONNECTS_FOR_CONNECTION_POINT = "GET_CROSSCONNECTS_FOR_CONNECTION_POINT";

  public static final String GET_LINKS_FOR_CONNECTION_POINT_BY_LAYER = "GET_LINKS_FOR_CONNECTION_POINT_BY_LAYER";

  public static final String GET_CROSSCONNECTS_FOR_CONNECTION_POINT_BY_LAYER = "GET_CROSSCONNECTS_FOR_CONNECTION_POINT_BY_LAYER";

  public ConnectionPointDBImpl() {
  }

  public ConnectionPointDBImpl(TopologyManager manager, TopologyElementDBImpl parent) {
    super(manager);
    this.parent = parent;
  }

  @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @JoinColumn(name="parent", referencedColumnName = "id")
  private TopologyElementDBImpl parent;

  @Override
  public Set<Connection> getConnections() {
    EntityManager manager = EntityManagerFactoryHelper.getEntityManager();
    Query query = manager.createNamedQuery(ConnectionPointDBImpl.GET_CONNECTIONS_FOR_CONNECTION_POINT);
    query.setParameter("cp", this);
    Set<Connection> connSet = new HashSet<>();
    connSet.addAll(query.getResultList());
    return connSet;
  }

  @Override
  public <T extends Connection> Set<T> getConnections(Class<T> instance) {
    Set<T> connSet = new HashSet<>();
    if (instance==null)
      return connSet;
    EntityManager manager = EntityManagerFactoryHelper.getEntityManager();
    Query query;
    if (Link.class.isAssignableFrom(instance))
      query = manager.createNamedQuery(ConnectionPointDBImpl.GET_LINKS_FOR_CONNECTION_POINT);
    else if (CrossConnect.class.isAssignableFrom(instance))
      query = manager.createNamedQuery(ConnectionPointDBImpl.GET_CROSSCONNECTS_FOR_CONNECTION_POINT);
    else
      query = manager.createNamedQuery(ConnectionPointDBImpl.GET_CONNECTIONS_FOR_CONNECTION_POINT);
    query.setParameter("cp", this);
    connSet.addAll(query.getResultList());
    return connSet;
  }

  @Override
  public Set<Connection> getConnections(NetworkLayer layer) {
    Set<Connection> connSet = new HashSet<>();
    if (layer==null)
      return connSet;
    EntityManager manager = EntityManagerFactoryHelper.getEntityManager();
    Query query = manager.createNamedQuery(ConnectionPointDBImpl.GET_CONNECTIONS_FOR_CONNECTION_POINT_BY_LAYER);
    query.setParameter("cp", this);
    query.setParameter("layer", layer);
    connSet.addAll(query.getResultList());
    return connSet;
  }

  @Override
  public <T extends Connection> Set<T> getConnections(NetworkLayer layer, Class<T> instance) {
    Set<T> connSet = new HashSet<>();
    if (instance==null)
      return connSet;
    if (layer==null)
      return connSet;
    EntityManager manager = EntityManagerFactoryHelper.getEntityManager();
    Query query;
    if (Link.class.isAssignableFrom(instance))
      query = manager.createNamedQuery(ConnectionPointDBImpl.GET_LINKS_FOR_CONNECTION_POINT_BY_LAYER);
    else if (CrossConnect.class.isAssignableFrom(instance))
      query = manager.createNamedQuery(ConnectionPointDBImpl.GET_CROSSCONNECTS_FOR_CONNECTION_POINT_BY_LAYER);
    else
      query = manager.createNamedQuery(ConnectionPointDBImpl.GET_CONNECTIONS_FOR_CONNECTION_POINT_BY_LAYER);
    query.setParameter("cp", this);
    query.setParameter("layer", layer);
    connSet.addAll(query.getResultList());
    return connSet;
  }

  @Override
  public TopologyElement getParent() {
    return parent;
  }
}
