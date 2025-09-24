package io.github.mohitc.topology.impl.primitives.connection;

import io.github.mohitc.topology.primitives.*;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class CrossConnectTest extends ConnectionTest {

  @Test
  public void testCrossConnectCycle() {

    TopologyManager manager = getTopologyManager();
    NetworkElement ne1 = null;
    ConnectionPoint cp1 =null, cp2=null;
    try {
      ne1 = manager.createNetworkElement();
      cp1 = manager.createConnectionPoint(ne1);
      cp2 = manager.createConnectionPoint(ne1);
    } catch (TopologyException e) {
      fail("Error when creating test.topology elements for creating a link");
    }

    assertNotNull(ne1);
    assertNotNull(cp1);
    assertNotNull(cp2);

    log.info("Attempting to create a new cross connect");

    CrossConnect cc = null;
    try {
      cc = manager.createCrossConnect(cp1.getID(), cp2.getID());
    } catch (TopologyException e) {
      fail("Error when creating a cross connect between the connection points");
    }

    assertNotNull(cc);

    checkConnectionFunctions(manager, cc, CrossConnect.class);

    checkConnectionAssociation(manager, cc, ne1, CrossConnect.class);
    cc.setLayer(NetworkLayer.OTS);
    checkConnectionAssociation(manager, cc, ne1, CrossConnect.class);



    log.info("Checking if connection points have the association with the cross connect");
    checkConnectionAssociation(manager, cc, cp1, CrossConnect.class);
    checkConnectionAssociation(manager, cc, cp2, CrossConnect.class);

  }

  @Test
  public void testInvalidCrossConnect() {
    TopologyManager manager = getTopologyManager();
    NetworkElement ne1 = null, ne2 = null;
    ConnectionPoint cp1 =null, cp2=null;
    try {
      ne1 = manager.createNetworkElement();
      ne2 = manager.createNetworkElement();
      cp1 = manager.createConnectionPoint(ne1);
      cp2 = manager.createConnectionPoint(ne2);
    } catch (TopologyException e) {
      fail("Error when creating test.topology elements for creating a link");
    }

    assertNotNull(ne1);
    assertNotNull(ne2);
    assertNotNull(cp1);
    assertNotNull(cp2);

    log.info("Attempting to create a cross connect between two connection points in different network elements");
    try {
      manager.createCrossConnect(cp1.getID(), cp2.getID());
      fail("Cross Connect should not be created connection points in different network elements");
    } catch (TopologyException e) {
      log.debug("Failed to create a cross connect between connection points {} and {} as expected", cp1, cp2);
    }

  }

}
