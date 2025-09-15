package io.github.mohitc.topology.impl.db.primitives;


import io.github.mohitc.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.TopologyManagerFactory;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.*;

public class TopologyManagerFactoryDBImpl implements TopologyManagerFactory{

  private static final Logger log = LoggerFactory.getLogger(TopologyManagerFactoryDBImpl.class);

  @Override
  public TopologyManager createTopologyManager(String id) throws TopologyException {
    if (id==null) {
      throw new TopologyException("ID cannot be null");
    }
    try(EntityManager em = EntityManagerFactoryHelper.getEntityManager()) {
      try {
        TopologyManager manager = em.find(TopologyManagerDBImpl.class, id);
        if (manager != null) {
          throw new TopologyException("ID is not unique, a topology manager instance with ID: " + id + " exists");
        }
      } catch (EntityNotFoundException | NoResultException e) {
        //Do nothing
        if (log.isDebugEnabled()) {
          log.debug("TopologyManager with ID {} does not exist. Creating new instance", id);
        }
      }
      //Entity does not exist, create one
      em.getTransaction().begin();
      TopologyManagerDBImpl managerDB = new TopologyManagerDBImpl(id);
      em.persist(managerDB);
      em.getTransaction().commit();
      return managerDB;
    }
  }

  @Override
  public TopologyManager getTopologyManager(String id) throws TopologyException {
    if (id==null) {
      throw new TopologyException("ID cannot be null");
    }
    try (EntityManager em = EntityManagerFactoryHelper.getEntityManager()){
      TopologyManager manager = em.find(TopologyManagerDBImpl.class, id);
      if (manager!=null) {
        return manager;
      } else {
        throw new TopologyException("Topology manager with ID : " + id + " does not exist");
      }
    } catch (EntityNotFoundException | NoResultException e) {
      throw new TopologyException("Topology manager with ID : " + id + " does not exist", e);
    }
  }

  @Override
  public boolean hasTopologyManager(String id) {
    if (id==null) {
      log.error("ID cannot be null");
      return false;
    }
    try (EntityManager em = EntityManagerFactoryHelper.getEntityManager()){
      TopologyManager manager = em.find(TopologyManagerDBImpl.class, id);
      return manager!=null;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public void removeTopologyManager(String id) throws TopologyException {
    if (id==null) {
      throw new TopologyException("ID cannot be null");
    }
    if (hasTopologyManager(id)) {
      try (EntityManager em = EntityManagerFactoryHelper.getEntityManager()) {
        TopologyManagerDBImpl managerDB = em.find(TopologyManagerDBImpl.class, id);
        em.getTransaction().begin();
        em.remove(managerDB);
        //TODO Include code to remove all child entities
        em.getTransaction().commit();
      }
    } else {
      throw new TopologyException("Topology manager with ID : " + id + " does not exist");
    }
  }
}
