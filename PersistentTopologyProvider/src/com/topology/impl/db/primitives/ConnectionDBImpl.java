package com.topology.impl.db.primitives;

import com.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import com.topology.impl.db.primitives.converters.NetworkLayerConverter;
import com.topology.primitives.Connection;
import com.topology.primitives.ConnectionPoint;
import com.topology.primitives.NetworkLayer;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.resource.ResourceException;
import com.topology.primitives.resource.ConnectionResource;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "connection")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
    @NamedQuery(name=ConnectionDBImpl.GET_CONNECTIONS_BY_LAYER, query = "SELECT p FROM ConnectionDBImpl p WHERE (p.layer = :layer)"),
    @NamedQuery(name=ConnectionDBImpl.GET_LINKS_BY_LAYER, query = "SELECT p FROM LinkDBImpl p WHERE (p.layer = :layer)"),
    @NamedQuery(name=ConnectionDBImpl.GET_CROSS_CONNECTS_BY_LAYER, query = "SELECT p FROM CrossConnectDBImpl p WHERE (p.layer = :layer)"),
})
abstract public class ConnectionDBImpl extends TopologyElementDBImpl implements Connection {

  public static final String GET_CONNECTIONS_BY_LAYER = "GET_CONNECTIONS_BY_LAYER";

  public static final String GET_LINKS_BY_LAYER = "GET_LINKS_BY_LAYER";

  public static final String GET_CROSS_CONNECTS_BY_LAYER = "GET_CROSS_CONNECTS_BY_LAYER";

  @OneToOne
  @JoinColumn(name="a_end_id", referencedColumnName = "id")
  private ConnectionPointDBImpl aEnd;

  @OneToOne
  @JoinColumn(name="z_end_id", referencedColumnName = "id")
  private ConnectionPointDBImpl zEnd;

  @Column(name="directed")
  private boolean directed;

  @Column(name="layer")
  @Convert(converter= NetworkLayerConverter.class)
  private NetworkLayer layer;

  public ConnectionDBImpl(){
  }

  public ConnectionDBImpl(TopologyManager manager, ConnectionPointDBImpl aEnd, ConnectionPointDBImpl zEnd) {
    super(manager);
    this.aEnd = aEnd;
    this.zEnd = zEnd;
  }

  @Override
  public ConnectionPoint getaEnd() {
    return aEnd;
  }

  @Override
  public ConnectionPoint getzEnd() {
    return zEnd;
  }

  @Override
  public boolean isDirected() {
    return directed;
  }

  @Override
  public void setDirected(boolean directed) {
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    em.getTransaction().begin();
    ConnectionDBImpl conn = em.find(ConnectionDBImpl.class, this.getID());
    conn.setIsDirected(directed);
    em.getTransaction().commit();
    em.close();
  }

  private void setIsDirected(boolean directed) {
    this.directed = directed;
  }

  @Override
  public NetworkLayer getLayer() {
    return layer;
  }

  @Override
  public void setLayer(NetworkLayer layer) {
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    em.getTransaction().begin();
    ConnectionDBImpl conn = em.find(ConnectionDBImpl.class, this.getID());
    conn.setNetworkLayer(layer);
    em.getTransaction().commit();
    this.setNetworkLayer(layer);
    em.close();
  }

  private void setNetworkLayer(NetworkLayer layer) {
    this.layer = layer;
  }

  @Override
  public ConnectionResource getTotalResources() throws ResourceException {
    return null;
  }

  @Override
  public void setTotalResources(ConnectionResource resource) throws ResourceException, UnsupportedOperationException {

  }

  @Override
  public ConnectionResource getAvailableResources() throws ResourceException, UnsupportedOperationException {
    return null;
  }

  @Override
  public ConnectionResource getReservedResources() throws ResourceException, UnsupportedOperationException {
    return null;
  }

  @Override
  public Map<Integer, ConnectionResource> getReservations() throws ResourceException, UnsupportedOperationException {
    return null;
  }

  @Override
  public boolean canReserve(ConnectionResource resource) throws ResourceException, UnsupportedOperationException {
    return false;
  }

  @Override
  public void reserveService(int connID, ConnectionResource resource) throws ResourceException, UnsupportedOperationException {

  }

  @Override
  public void releaseService(int connID) throws ResourceException, UnsupportedOperationException {

  }
}
