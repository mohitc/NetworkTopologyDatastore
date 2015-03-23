package com.topology.impl.db.primitives;

import com.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import com.topology.impl.db.primitives.converters.NetworkLayerConverter;
import com.topology.primitives.Connection;
import com.topology.primitives.ConnectionPoint;
import com.topology.primitives.NetworkLayer;
import com.topology.primitives.exception.resource.ResourceException;
import com.topology.primitives.resource.ConnectionResource;
import com.topology.primitives.resource.ConnectionResourceType;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "connection")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract public class ConnectionDBImpl extends TopologyElementDBImpl implements Connection {

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
    this.directed=directed;
    em.getTransaction().commit();
    em.close();
  }

  @Override
  public NetworkLayer getLayer() {
    return layer;
  }

  @Override
  public void setLayer(NetworkLayer layer) {
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    em.getTransaction().begin();
    this.layer = layer;
    em.getTransaction().commit();
    em.close();
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
