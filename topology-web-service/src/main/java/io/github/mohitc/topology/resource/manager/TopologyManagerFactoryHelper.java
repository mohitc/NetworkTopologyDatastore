package io.github.mohitc.topology.resource.manager;

import io.github.mohitc.topology.impl.primitives.TopologyManagerFactoryImpl;
import io.github.mohitc.topology.primitives.TopologyManagerFactory;

public final class TopologyManagerFactoryHelper {
  private TopologyManagerFactoryHelper() {}

  /**
   * This private static inner class is loaded only when getInstance() is called for the first time.
   * The JVM guarantees that the class initialization is thread-safe.
   */
  private static final class InstanceHolder {
    private static final TopologyManagerFactory INSTANCE = new TopologyManagerFactoryImpl();
  }

  public static TopologyManagerFactory getInstance() {
    return InstanceHolder.INSTANCE;
  }
}
