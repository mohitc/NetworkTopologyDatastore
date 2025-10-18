package io.github.mohitc.topology.impl.db.persistencehelper;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Map;
import java.util.Objects;

public final class EntityManagerFactoryHelper {
  private static EntityManagerFactory factory;
  private static final Object lock = new Object();

  private EntityManagerFactoryHelper() {}

  public static void init(Map<String, String> properties) {
    if (factory == null) {
      synchronized (lock) {
          factory = Persistence.createEntityManagerFactory("TopologyProvider", properties);
      }
    }
  }

  public static EntityManager getEntityManager() {
    Objects.requireNonNull(factory, "EntityManagerFactory has not been initialized. Call init() first.");
    return factory.createEntityManager();
  }

  public static void close() {
    if (factory != null) {
      synchronized (lock) {
        if (factory != null && factory.isOpen()) {
          factory.close();
          factory = null;
        }
      }
    }
  }
}
