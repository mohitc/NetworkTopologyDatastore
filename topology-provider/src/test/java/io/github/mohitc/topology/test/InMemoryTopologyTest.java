package io.github.mohitc.topology.test;

import io.github.mohitc.topology.impl.primitives.TopologyManagerFactoryImpl;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.TopologyManagerFactory;

public class InMemoryTopologyTest extends AbstractTopologyTest {
    @Override
    public void init() {
        // No-op
    }

    @Override
    public TopologyManagerFactory getTopologyManagerFactory() {
        return new TopologyManagerFactoryImpl();
    }

    @Override
    public TopologyManager getTopologyManager() {
      try {
        return getTopologyManagerFactory().createTopologyManager("default");
      } catch (Exception e) {
        throw new RuntimeException("Unexpected exception during testing:", e);
      }
    }

    @Override
    public void close() {
        // No-op
    }
}
