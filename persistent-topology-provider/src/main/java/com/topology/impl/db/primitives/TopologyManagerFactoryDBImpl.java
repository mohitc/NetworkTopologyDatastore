package com.topology.impl.db.primitives;


import com.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.TopologyManagerFactory;
import com.topology.primitives.exception.TopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class TopologyManagerFactoryDBImpl implements TopologyManagerFactory{

  private static final Logger log = LoggerFactory.getLogger(TopologyManagerFactoryDBImpl.class);

  @Override
  public TopologyManager createTopologyManager(String id) throws TopologyException {
    if (id==null)
      throw new TopologyException("ID cannot be null");
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    try {
      TopologyManager manager = em.find((TopologyManagerDBImpl.class), id);
      if (manager!=null)
        throw new TopologyException("ID is not unique, a topology manager instance with ID: "+ id + " exists");
    } catch (EntityNotFoundException | NoResultException e) {
      //Do nothing
    }
    //Entity does not exist, create one
    em.getTransaction().begin();
    TopologyManagerDBImpl managerDB = new TopologyManagerDBImpl(id);
    em.persist(managerDB);
    em.getTransaction().commit();
    return managerDB;
  }

  @Override
  public TopologyManager getTopologyManager(String id) throws TopologyException {
    if (id==null)
      throw new TopologyException("ID cannot be null");
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    try {
      TopologyManager manager = em.find((TopologyManagerDBImpl.class), id);
      if (manager!=null)
        return manager;
      else
        throw new TopologyException("Topology manager with ID : " + id + " does not exist");
    } catch (EntityNotFoundException | NoResultException e) {
      throw new TopologyException("Topology manager with ID : " + id + " does not exist");
    }
  }

  @Override
  public boolean hasTopologyManager(String id) {
    if (id==null) {
      log.error("ID cannot be null");
      return false;
    }
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
    try {
      TopologyManager manager = em.find((TopologyManagerDBImpl.class), id);
      return manager!=null;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public void removeTopologyManager(String id) throws TopologyException {
    if (id==null)
      throw new TopologyException("ID cannot be null");
    if (hasTopologyManager(id)) {
      EntityManager em = EntityManagerFactoryHelper.getEntityManager();
      TopologyManagerDBImpl managerDB = em.find(TopologyManagerDBImpl.class, id);
      em.getTransaction().begin();
      em.remove(managerDB);
      //TODO Include code to remove all child entities
      em.getTransaction().commit();
    } else {
      throw new TopologyException("Topology manager with ID : " + id + " does not exist");
    }
  }
}
