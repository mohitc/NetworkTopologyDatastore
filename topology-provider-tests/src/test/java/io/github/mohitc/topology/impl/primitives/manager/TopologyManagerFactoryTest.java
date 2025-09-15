package io.github.mohitc.topology.impl.primitives.manager;

import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.TopologyManagerFactory;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class TopologyManagerFactoryTest {

  public static final Logger log = LoggerFactory.getLogger(TopologyManagerFactoryTest.class);

  public TopologyManagerFactory getTopologyManagerFactoryInstance() {
    return TopoManagerHelper.getFactoryInstance();
  }

  @Test
  public void testTopologyManagerFactory() {
    log.info("Starting test for Topology Manager factory");
    log.debug("Initially there should be no topology manager instance available");

    TopologyManagerFactory factory = getTopologyManagerFactoryInstance();

    if (factory==null)
      fail("The method to generate a factory instance returns a null object");

    String id = "1234";

    assertFalse(factory.hasTopologyManager(id), "Factory should not have a manager instance before it is created");

    try {
      factory.getTopologyManager(id);
      fail("Without an explicit creation of a topology manager, instance should not be provided");
    } catch (TopologyException e) {
      log.info("Correct operation (exception) when a manager with the specified ID is not available");
    }

    //create a topology manager instance
    try {
      TopologyManager manager = factory.createTopologyManager(id);
      assertNotNull(manager);
      log.info("Manager created successfully");
      //manager is not null
      assertTrue(factory.hasTopologyManager(id), "hasTopologyManager(id) returns false after creation of manager instance");
      log.info("Topology manager instance with ID: {} found in Factory", id);
    } catch (TopologyException e) {
      fail("Topology Manager creation failed: " + e.getMessage());
    }

    try {
      TopologyManager manager = factory.getTopologyManager(id);
      assertNotNull(manager, "Manager instance cannot be null");
      log.info("Correct operation (manager object) when a manager with the specified ID is available");

    } catch (TopologyException e) {
      fail("Without an explicit creation of a topology manager, instance should not be provided");
    }

    //remove the topology manager
    try {
      factory.removeTopologyManager(id);
      assertFalse(factory.hasTopologyManager(id), "Factory still contains manager instance after removal");
    } catch (TopologyException e) {
      fail("Exception when attempting to remove the topology manager: " + e.getMessage());
    }
  }

}
