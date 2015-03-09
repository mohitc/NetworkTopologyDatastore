package com.topology.impl.db.primitives;

import com.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import com.topology.primitives.*;
import com.topology.primitives.exception.TopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TopologyManagerDBImpl implements TopologyManager {

  private static final Logger log = LoggerFactory.getLogger(TopologyManagerDBImpl.class);

  private String instance;

  public TopologyManagerDBImpl(String instance) {
    this.instance = instance;
  }

  @Override
  public String getIdentifier() {
    return instance;
  }

  @Override
  public boolean hasElement(int id) {
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    try {
      TopologyElementDBImpl te = em.find(TopologyElementDBImpl.class, id);
      if (te!=null)
        return true;
    } catch (Exception e) {
      log.error("Exception when looking for entity with id: " + id, e);
    }
    return false;
  }

  private <T extends TopologyElementDBImpl, Q extends TopologyElement> Class<T> getComparableDbClass(Class<Q> instance) {
    if (instance==null) {
      log.error("Class instance for conversin cannot be null, defaulting to topology element base class");
    } else {
      if (NetworkElement.class.isAssignableFrom(instance)) {
        return (Class<T>) NetworkElementDBImpl.class;
      } else if (ConnectionPoint.class.isAssignableFrom(instance)) {
        if (Port.class.isAssignableFrom(instance)) {
          return (Class<T>) PortDBImpl.class;
        } else {
          return (Class<T>) ConnectionPointDBImpl.class;
        }
      } else if (Connection.class.isAssignableFrom(instance)) {
        if (Link.class.isAssignableFrom(instance)) {
          return (Class<T>) LinkDBImpl.class;
        } else if (CrossConnect.class.isAssignableFrom(instance)) {
          return (Class<T>) CrossConnectDBImpl.class;
        } else if (Trail.class.isAssignableFrom(instance)) {
          return (Class<T>) TrailDBImpl.class;
        } else {
          return (Class<T>) ConnectionDBImpl.class;
        }
      }
    }
    return (Class<T>) TopologyElementDBImpl.class;
  }

  @Override
  public <T extends TopologyElement> boolean hasElement(int id, Class<T> instance) {
    Class<? extends TopologyElementDBImpl> dbInstance = getComparableDbClass(instance);
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    try {
      Object te = em.find(dbInstance, id);
      if (te!=null)
        return true;
    } catch (Exception e) {
      log.error("Exception when looking for entity with id: " + id, e);
    }
    return false;
  }

  @Override
  public <T extends TopologyElement> Set<T> getAllElements(Class<T> instance) {
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    //em.
    return null;
  }

  @Override
  public NetworkElement createNetworkElement() throws TopologyException {
    return null;
  }

  @Override
  public ConnectionPoint createConnectionPoint(TopologyElement parent) throws TopologyException {
    return null;
  }

  @Override
  public Port createPort(TopologyElement parent) throws TopologyException {
    return null;
  }

  @Override
  public Link createLink(int startCpID, int endCpID) throws TopologyException {
    return null;
  }

  @Override
  public CrossConnect createCrossConnect(int startCpID, int endCpID) throws TopologyException {
    return null;
  }

  @Override
  public Trail createTrail(int startCpID, int endCpID) throws TopologyException {
    return null;
  }

  @Override
  public void removeTopologyElement(int id) throws TopologyException {

  }

  @Override
  public void removeNetworkElement(int id) throws TopologyException {

  }

  @Override
  public void removeConnectionPoint(int id) throws TopologyException {

  }

  @Override
  public void removePort(int id) throws TopologyException {

  }

  @Override
  public void removeConnection(int id) throws TopologyException {

  }

  @Override
  public void removeLink(int id) throws TopologyException {

  }

  @Override
  public void removeCrossConnect(int id) throws TopologyException {

  }

  @Override
  public void removeTrail(int id) throws TopologyException {

  }

  @Override
  public TopologyElement getElementByID(int id) throws TopologyException {
    return null;
  }

  @Override
  public <T extends TopologyElement> T getElementByID(int id, Class<T> instance) throws TopologyException {
    return null;
  }

  @Override
  public <T extends TopologyElement> Set<T> getElementsByLabel(String label, Class<T> instance) {
    return null;
  }

  @Override
  public <T extends TopologyElement> T getSingleElementByLabel(String label, Class<T> instance) throws TopologyException {
    return null;
  }

  @Override
  public Set<Connection> getConnections(int startNeID, int endNeID) throws TopologyException {
    return null;
  }

  @Override
  public <T extends Connection> Set<T> getConnections(int startNeID, int endNeID, Class<T> instance) throws TopologyException {
    return null;
  }

  @Override
  public Set<Connection> getConnections(int startNeID, int endNeID, NetworkLayer layer) throws TopologyException {
    return null;
  }

  @Override
  public <T extends Connection> Set<T> getConnections(int startNeID, int endNeID, NetworkLayer layer, Class<T> instance) throws TopologyException {
    return null;
  }

  @Override
  public Set<Connection> getConnections(int startNeID, int endNeID, boolean isDirected) throws TopologyException {
    return null;
  }

  @Override
  public <T extends Connection> Set<T> getConnections(int startNeID, int endNeID, boolean isDirected, Class<T> instance) throws TopologyException {
    return null;
  }

  @Override
  public Set<Connection> getConnections(int startNeID, int endNeID, boolean isDirected, NetworkLayer layer) throws TopologyException {
    return null;
  }

  @Override
  public <T extends Connection> Set<T> getConnections(int startNeID, int endNeID, boolean isDirected, NetworkLayer layer, Class<T> instance) throws TopologyException {
    return null;
  }

  @Override
  public Path generatePathDef(ConnectionPoint aEnd, ConnectionPoint zEnd, List<Connection> forwardSequence, List<Connection> backwardSequence, boolean directed, boolean isStrict) {
    return null;
  }
}
