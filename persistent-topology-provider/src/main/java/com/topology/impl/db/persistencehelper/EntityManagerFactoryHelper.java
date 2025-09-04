package com.topology.impl.db.persistencehelper;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryHelper {
  private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("TopologyProvider");

  public static EntityManager getEntityManager() {
    return factory.createEntityManager();
  }
}
