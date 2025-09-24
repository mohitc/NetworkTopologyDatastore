package io.github.mohitc.topology.impl.primitives.manager;

import io.github.mohitc.topology.primitives.NetworkElement;
import io.github.mohitc.topology.primitives.TopologyElement;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TopologyManagerTest {

  private static final Logger log = LoggerFactory.getLogger(TopologyManagerTest.class);

  protected TopologyManager getTopologyManager() {
    return TopoManagerHelper.getInstance();
  }

  @Test
  public void testGetFunctions() {

    TopologyManager manager = getTopologyManager();
    log.info("Creating network element");

    NetworkElement element = null;
    try {
      element = manager.createNetworkElement();
    } catch (TopologyException e) {
      fail("Error while creating network element: " + e.getMessage());
    }

    //Element was created successfully, test get functions
    try {
      log.info("testing getElementByID(id)");
      assertEquals(element, manager.getElementByID(element.getID()));
      log.info("testing getElementByID(id, Class<T>)");
      assertEquals(element, manager.getElementByID(element.getID(), NetworkElement.class));
      log.info("testing getAllElementByID(Class<T>)");
      assert(manager.getAllElements(TopologyElement.class).contains(element));
      log.info("testing getAllElementByID(Class<T>)");
      assert(manager.getAllElements(NetworkElement.class).contains(element));
    } catch (TopologyException e) {
      fail("Error while fetching test.topology elements from the test.topology manager: " + e.getMessage());
    }

    //Creating connection point
    log.info("Creating connection point and port");
    try {
      manager.createNetworkElement();
    } catch (TopologyException e) {
      fail("Error while creating network element: " + e.getMessage());
    }

  }
}
