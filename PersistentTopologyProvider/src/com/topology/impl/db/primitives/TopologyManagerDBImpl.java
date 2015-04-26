package com.topology.impl.db.primitives;

import com.topology.dto.PathDTO;
import com.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import com.topology.primitives.*;
import com.topology.primitives.exception.TopologyException;
import com.topology.primitives.resource.ConnectionResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;

public class TopologyManagerDBImpl implements TopologyManager {

  private static final Logger log = LoggerFactory.getLogger(TopologyManagerDBImpl.class);

  private String instance;

  private EntityManager getEntityManager() {
    return EntityManagerFactoryHelper.getEntityManager();
  }

  public TopologyManagerDBImpl(String instance) {
    this.instance = instance;
  }

  @Override
  public String getIdentifier() {
    return instance;
  }

  @Override
  public boolean hasElement(int id) {
    EntityManager em = getEntityManager();
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
      log.error("Class instance for conversion cannot be null, defaulting to topology element base class");
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
    EntityManager em = getEntityManager();
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
    Set<T> resultSet = new HashSet<>();
    try {
      EntityManager em = getEntityManager();
      Class t = getComparableDbClass(instance);
      String q = "Select q FROM " + t.getSimpleName() + " q";
      Query query = em.createQuery(q);
      resultSet.addAll(query.getResultList());
    } catch (EntityNotFoundException e) {
      log.error("Could not find entities in the database", e);
    }
    return resultSet;
  }

  @Override
  public NetworkElement createNetworkElement() throws TopologyException {
    EntityManager em = getEntityManager();
    em.getTransaction().begin();
    NetworkElement ne = new NetworkElementDBImpl(this);
    em.persist(ne);
    em.getTransaction().commit();
    log.info("New Network Element Created: " + ne);
    return ne;
  }

  @Override
  public ConnectionPoint createConnectionPoint(TopologyElement parent) throws TopologyException {
    EntityManager em = getEntityManager();
    em.getTransaction().begin();
    TopologyElementDBImpl parentElement = null;
    if (parent!=null)
      parentElement = em.find(TopologyElementDBImpl.class, parent.getID());
    ConnectionPoint cp = new ConnectionPointDBImpl(this, parentElement);
    em.persist(cp);
    em.getTransaction().commit();
    log.info("New Connection Point Created: " + cp);
    return cp;
  }

  @Override
  public Port createPort(TopologyElement parent) throws TopologyException {
    EntityManager em = getEntityManager();
    em.getTransaction().begin();
    TopologyElementDBImpl parentElement = null;
    if (parent!=null)
      parentElement = em.find(TopologyElementDBImpl.class, parent.getID());
    Port cp = new PortDBImpl(this, parentElement);
    em.persist(cp);
    em.getTransaction().commit();
    log.info("New Port Created: " + cp);
    return cp;
  }

  @Override
  public Link createLink(int startCpID, int endCpID) throws TopologyException {
    //Check if start and end connection points are in the topology manager
    //Endpoints of a link should have different network elements
    EntityManager em = getEntityManager();
    try {
      ConnectionPointDBImpl startCp = em.find(ConnectionPointDBImpl.class, startCpID);
      ConnectionPointDBImpl endCp = em.find(ConnectionPointDBImpl.class, endCpID);
      NetworkElement startNe = getNetworkElementFromCp(startCp);
      NetworkElement endNe = getNetworkElementFromCp(endCp);
      if ((startNe != null) && (endNe != null) && (startNe == endNe)) {
        throw new TopologyException("Endpoints of a link must belong to different network elements");
      }
      em.getTransaction().begin();
      Link link = new LinkDBImpl(this, startCp, endCp);
      em.persist(link);
      em.getTransaction().commit();
      return link;
    } catch (EntityNotFoundException e){
      throw new TopologyException("Start or end connection point not found");
    }
  }

  @Override
  public CrossConnect createCrossConnect(int startCpID, int endCpID) throws TopologyException {
    //Check if start and end connection points are in the topology manager
    //Endpoints of a cross connect should have the same network element
    EntityManager em = getEntityManager();
    try {
      ConnectionPointDBImpl startCp = em.find(ConnectionPointDBImpl.class, startCpID);
      ConnectionPointDBImpl endCp = em.find(ConnectionPointDBImpl.class, endCpID);
      NetworkElement startNe = getNetworkElementFromCp(startCp);
      NetworkElement endNe = getNetworkElementFromCp(endCp);
      if ((startNe == null) || (endNe == null)) {
        throw new TopologyException("Endpoints of a cross connect must belong to a network element");
      }
      if (startNe!=endNe) {
        throw new TopologyException("Endpoints of a cross connect must belong to a network element");
      }
      em.getTransaction().begin();
      CrossConnect cc = new CrossConnectDBImpl(this, startCp, endCp);
      em.persist(cc);
      em.getTransaction().commit();
      return cc;
    } catch (EntityNotFoundException e){
      throw new TopologyException("Start or end connection point not found");
    }
  }

  @Override
  public Trail createTrail(int startCpID, int endCpID, PathDTO pathDTO, boolean directed, ConnectionResource resource, NetworkLayer layer) throws TopologyException {
    return null;
  }

  @Override
  public NetworkElement getNetworkElementFromCp(ConnectionPoint cp) {
    while ((cp != null) && (cp.getParent() != null)) {
      if (NetworkElement.class.isAssignableFrom(cp.getParent().getClass())) {
        return (NetworkElement) cp.getParent();
      } else if (ConnectionPoint.class.isAssignableFrom(cp.getParent().getClass())) {
        cp = (ConnectionPoint) cp.getParent();
      } else {
        log.error("Parent of connection point is not a connection point or a Network Element");
        return null;
      }
    }
    return null;
  }

  @Override
  public void removeTopologyElement(int id) throws TopologyException {
    TopologyElement te = getElementByID(id);
    if (te != null) {
      if (NetworkElement.class.isAssignableFrom(te.getClass())) {
        removeNetworkElement(id);
      } else if (ConnectionPoint.class.isAssignableFrom(te.getClass())) {
        removeConnectionPoint(id);
      } else if (Connection.class.isAssignableFrom(te.getClass())) {
        removeConnection(id);
      }
    } else {
      log.error("Element not found in TopoElements map");
      throw new TopologyException("Element not found in TopoElements map");
    }
  }

  @Override
  public void removeNetworkElement(int id) throws TopologyException {
    EntityManager em = getEntityManager();
    try {
      em.getTransaction().begin();
      NetworkElementDBImpl ne = em.find(NetworkElementDBImpl.class, id);
      Set<ConnectionPoint> neCps = ne.getConnectionPoints(false);
      if ((neCps != null) && (neCps.size() > 0)) {
        throw new TopologyException("Network element cannot be deleted while it contains connection points");
      }
      em.remove(ne);
      em.getTransaction().commit();
      log.info("Network Element removed successfully");
    }catch (EntityNotFoundException e) {
      throw new TopologyException("Element with ID: " + id + " does not exist or is not an instance of NetworkElement");
    }
  }

  @Override
  public void removeConnectionPoint(int id) throws TopologyException {
    EntityManager em = getEntityManager();
    try {
      ConnectionPoint cp = em.find(ConnectionPointDBImpl.class, id);
      //Connection point exists
      if (Port.class.isAssignableFrom(cp.getClass())) {
        //check for contained connection points
        Port port = (Port) cp;
        if (!((port.getContainedConnectionPoints() == null) || (port.getContainedConnectionPoints().size() == 0))) {
          throw new TopologyException("Port contains connection points that must be removed before removing port");
        }
      }
      //Connection point is not a port
      //Check if there are any existing connections from the connection point
      if ((cp.getConnections() == null) || (cp.getConnections().size() == 0)) {
        //No connections on connection point
        em.getTransaction().begin();
        em.remove(cp);
        em.getTransaction().commit();
        log.info("Connection Point removed successfully");
      } else {
        throw new TopologyException("Connection point cannot be removed untill all connections have been removed from them");
      }
    } catch (EntityNotFoundException e) {
      throw new TopologyException("Element with ID: " + id + " does not exist or is not an instance of ConnectionPoint");
    }
  }

  @Override
  public void removePort(int id) throws TopologyException {
    removeConnectionPoint(id);
  }

  @Override
  public void removeConnection(int id) throws TopologyException {
    Connection te = getElementByID(id, Connection.class);
    if (te != null) {
      if (Link.class.isAssignableFrom(te.getClass())) {
        removeLink(id);
      } else if (CrossConnect.class.isAssignableFrom(te.getClass())) {
        removeCrossConnect(id);
      }
    } else {
      log.error("Element not found in TopoElements map");
      throw new TopologyException("Element not found in TopoElements map");
    }

  }

  @Override
  public void removeLink(int id) throws TopologyException {
    //TODO check for layer associations before deleting
    EntityManager em = getEntityManager();
    try {
      Link link = em.find(LinkDBImpl.class, id);
      em.getTransaction().begin();
      em.remove(link);
      em.getTransaction().commit();
        log.info("Link removed successfully");
    } catch (EntityNotFoundException e) {
      throw new TopologyException("Element with ID: " + id + " does not exist or is not an instance of Link");
    }
  }

  @Override
  public void removeCrossConnect(int id) throws TopologyException {
    //TODO check for layer associations before deleting
    EntityManager em = getEntityManager();
    try {
      CrossConnect cc = em.find(CrossConnectDBImpl.class, id);
      em.getTransaction().begin();
      em.remove(cc);
      em.getTransaction().commit();
      log.info("CrossConnect removed successfully");
    } catch (EntityNotFoundException e) {
      throw new TopologyException("Element with ID: " + id + " does not exist or is not an instance of CrossConnect");
    }

  }

  @Override
  public void removeTrail(int id) throws TopologyException {

  }

  @Override
  public TopologyElement getElementByID(int id) throws TopologyException {
    return getElementByID(id, TopologyElement.class);
  }

  @Override
  public <T extends TopologyElement> T getElementByID(int id, Class<T> instance) throws TopologyException {
    Class<? extends TopologyElementDBImpl> dbInstance = getComparableDbClass(instance);
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    try {
      Object te = em.find(dbInstance, id);
      if (te!=null)
        return (T) te;
    } catch (Exception e) {
      log.error("Exception when looking for entity with id: " + id, e);
    }
    throw new TopologyException("Element with ID: " + id + " does not exist or is not an instance of " + instance.getSimpleName());
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
  public Set<Connection> getAllConnections(NetworkLayer layer) throws TopologyException {
    EntityManager em = getEntityManager();
    Query query = em.createNamedQuery(ConnectionDBImpl.GET_CONNECTIONS_BY_LAYER);
    query.setParameter("layer", layer);
    Set<Connection> connSet = new HashSet<>();
    connSet.addAll(query.getResultList());
    return connSet;
  }

  @Override
  public <T extends Connection> Set<T> getAllConnections(Class<T> instance, NetworkLayer layer) throws TopologyException {
    if (instance==null) {
      throw new TopologyException("Class instance cannot be null");
    }
    String namedQuery;
    if (Link.class.isAssignableFrom(instance)) {
      namedQuery = ConnectionDBImpl.GET_LINKS_BY_LAYER;
    } else if (CrossConnect.class.isAssignableFrom(instance)) {
      namedQuery = ConnectionDBImpl.GET_CROSS_CONNECTS_BY_LAYER;
    } else {
      namedQuery = ConnectionDBImpl.GET_CONNECTIONS_BY_LAYER;
    }
    EntityManager em = getEntityManager();
    Query query = em.createNamedQuery(namedQuery);
    query.setParameter("layer", layer);
    Set<T> connSet = new HashSet<>();
    connSet.addAll(query.getResultList());
    return connSet;
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

}
