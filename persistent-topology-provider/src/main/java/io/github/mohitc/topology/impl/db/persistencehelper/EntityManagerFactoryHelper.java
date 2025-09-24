package io.github.mohitc.topology.impl.db.persistencehelper;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class EntityManagerFactoryHelper {
  private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("TopologyProvider");

  private EntityManagerFactoryHelper() {}

  public static EntityManager getEntityManager() {
    return factory.createEntityManager();
  }
}
