package com.topology.impl.primitives.algorithm;

import com.topology.algorithm.PathComputationAlgorithm;
import com.topology.algorithm.constraint.PathConstraint;
import com.topology.dto.PathDTO;
import com.topology.impl.primitives.manager.TopoManagerHelper;
import com.topology.primitives.*;
import com.topology.primitives.exception.TopologyException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class PathAlgorithmTest {

  private static final Logger log = LoggerFactory.getLogger(PathAlgorithmTest.class);

  private static TopologyManager getTopologyManager() {
    return TopoManagerHelper.getInstance();
  }

  @Test
  public void testSinglePathComputation() {
    log.info("Generating network elements");
    TopologyManager manager = getTopologyManager();
    NetworkElement ne1 = null , ne2 = null, ne3= null;
    ConnectionPoint aEnd = null, in1 = null, in2 = null, zEnd = null;
    Link link12=null, link23=null;
    CrossConnect c12=null;
    try {
      ne1 = manager.createNetworkElement();
      ne2 = manager.createNetworkElement();
      ne3 = manager.createNetworkElement();

      aEnd = manager.createConnectionPoint(ne1);
      in1 = manager.createConnectionPoint(ne2);
      in2 = manager.createConnectionPoint(ne2);
      zEnd = manager.createConnectionPoint(ne3);

      link12 = manager.createLink(aEnd.getID(), in1.getID());
      link12.setDirected(false);

      link23 = manager.createLink(in2.getID(), zEnd.getID());
      link23.setDirected(false);

      c12 = manager.createCrossConnect(in1.getID(), in2.getID());
      c12.setDirected(false);
    } catch (TopologyException e) {
      fail("Error in creating test.topology elements: " + e);
    }


    PathComputationAlgorithm algorithm = new PathComputationAlgorithm();
    PathDTO path = null;

    try {
      path = algorithm.computePath(manager, aEnd, zEnd, new PathConstraint(false, true));
      log.info("Computed path: " + path);
    } catch (TopologyException e) {
      fail("Path computation failed");
    }

    log.info("Computed path: " + path);
    assertEquals(3, path.getForwardConnectionSequence().size(), "Path size should be 2");

    log.info("Asymmetric path should also be computed over bidirectional links if" + path);
    try {
      path = algorithm.computePath(manager, aEnd, zEnd, new PathConstraint(false, false));
      log.info("Computed path: " + path);
    } catch (TopologyException e) {
      fail("Bidirectional asymmetric path computation should work in a network with only bidirectional links");
    }


    log.info("Switch links to directed");
    link12.setDirected(true);
    link23.setDirected(true);
    c12.setDirected(true);

    try {
      path = algorithm.computePath(manager, aEnd, zEnd, new PathConstraint(false, true));
      if (path!=null)
        fail("No Paths should be found");
    } catch (TopologyException e) {
      log.info("Bidirectional path computation in a network with only unidirectional paths should fail");
    }

    try {
      path = algorithm.computePath(manager, aEnd, zEnd, new PathConstraint(false, false));
      if (path!=null)
        fail("No Paths should be found");
    } catch (TopologyException e) {
      log.info("Bidirectional asymmetric path computation in a network with only unidirectional paths should fail");
    }

    //create directed links in the reverse direction
    log.info("Creating unidirectional links in the reverse direction");
    Link link21 = null, link32 = null;
    CrossConnect c21 = null;
    try {
      link21 = manager.createLink(in1.getID(), aEnd.getID());
      link21.setDirected(true);

      link32 = manager.createLink(zEnd.getID(), in2.getID());
      link32.setDirected(true);

      c21 = manager.createCrossConnect(in2.getID(), in1.getID());
      c21.setDirected(true);
    } catch (TopologyException e) {
      fail("Error when creating links in the reverse direction");
    }

    log.info("Bidirectional symmetric path computation should fail");
    try {
      path = algorithm.computePath(manager, aEnd, zEnd, new PathConstraint(false, true));
      fail("Symmetric bidirectional Path computation in a network with only unidirectional links should fail");
    } catch (TopologyException e) {
      log.info("Symmetric bidirectional Path computation in a network with only unidirectional links failed");
    }

    log.info("Bidirectional asymmetric path computation should succeed");
    try {
      path = algorithm.computePath(manager, aEnd, zEnd, new PathConstraint(false, false));
      log.info("Computed path: " + path);
    } catch (TopologyException e) {
      fail("asymmetric bidirectional Path computation in a network with only unidirectional links should not fail");
    }
  }
}
