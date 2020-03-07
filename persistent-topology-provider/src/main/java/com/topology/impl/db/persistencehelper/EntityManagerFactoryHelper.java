package com.topology.impl.db.persistencehelper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryHelper {
  private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("TopologyProvider");

  public static EntityManager getEntityManager() {
    return factory.createEntityManager();
  }
}
