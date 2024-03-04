package com.topology.impl.primitives.connection;

import com.topology.primitives.*;
import com.topology.primitives.exception.TopologyException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LinkTest extends ConnectionTest {

  private void checkTopoManagerConnAssociation(TopologyManager manager, Link link, NetworkElement ne1, NetworkElement ne2) {
    Set<Connection> connections;
    log.info("Checking getConnections(ne1, ne2)");
    try {
      connections = manager.getConnections(ne1.getID(), ne2.getID());
      assertNotNull(connections);
      assertTrue(connections.size()>0);
      assertTrue(connections.contains(link));
    } catch (TopologyException e) {
      fail("Link could not be extracted from network element association");
    }
    log.info("Checking getConnections(ne1, ne2, layer)");
    try {
      connections = manager.getConnections(ne1.getID(), ne2.getID(), link.getLayer());
      assertNotNull(connections);
      assertTrue(connections.size()>0);
      assertTrue(connections.contains(link));
    } catch (TopologyException e) {
      fail("Link could not be extracted from network element association");
    }
    Set<Link> links;
    log.info("Checking getConnections(ne1, ne2, Class<Instance>)");
    try {
      links = manager.getConnections(ne1.getID(), ne2.getID(), Link.class);
      assertNotNull(links);
      assertTrue(links.size()>0);
      assertTrue(links.contains(link));
    } catch (TopologyException e) {
      fail("Link could not be extracted from network element association");
    }

    log.info("Checking getConnections(ne1, ne2, layer, Class<Instance>)");
    try {
      links = manager.getConnections(ne1.getID(), ne2.getID(), link.getLayer(), Link.class);
      assertNotNull(links);
      assertTrue(links.size()>0);
      assertTrue(links.contains(link));
    } catch (TopologyException e) {
      fail("Link could not be extracted from network element association");
    }
  }

  private void checkTopoManagerDirectedConnectionAssociation(TopologyManager manager, Link link, NetworkElement ne1, NetworkElement ne2) {
    Set<Connection> connections;
    log.info("Checking getConnections(ne1, ne2, isDirected)");
    try {
      connections = manager.getConnections(ne1.getID(), ne2.getID(), link.isDirected());
      assertNotNull(connections);
      assertTrue(connections.size()>0);
      assertTrue(connections.contains(link));
      if(link.isDirected()){
        connections = manager.getConnections(ne2.getID(), ne1.getID(), link.isDirected());
        assertNotNull(connections);
        assertTrue(connections.size()>=0);
        assertFalse(connections.contains(link));
      } else {
        connections = manager.getConnections(ne2.getID(), ne1.getID(), link.isDirected());
        assertNotNull(connections);
        assertTrue(connections.size()>0);
        assertTrue(connections.contains(link));
      }
    } catch (TopologyException e) {
      fail("Link could not be extracted from network element association");
    }
    log.info("Checking getConnections(ne1, ne2, isDirected, layer)");
    try {
      connections = manager.getConnections(ne1.getID(), ne2.getID(), link.isDirected(), link.getLayer());
      assertNotNull(connections);
      assertTrue(!connections.isEmpty());
      assertTrue(connections.contains(link));
      if(link.isDirected()){
        connections = manager.getConnections(ne2.getID(), ne1.getID(), link.isDirected(), link.getLayer());
        assertNotNull(connections);
        assertTrue(connections.size()>=0);
        assertFalse(connections.contains(link));
      } else {
        connections = manager.getConnections(ne2.getID(), ne1.getID(), link.isDirected(), link.getLayer());
        assertNotNull(connections);
        assertTrue(connections.size()>0);
        assertTrue(connections.contains(link));
      }
    } catch (TopologyException e) {
      fail("Link could not be extracted from network element association");
    }
    Set<Link> links;
    log.info("Checking getConnections(ne1, ne2, isDirected, Class<Instance>)");
    try {
      links = manager.getConnections(ne1.getID(), ne2.getID(), link.isDirected(), Link.class);
      assertNotNull(links);
      assertTrue(!links.isEmpty());
      assertTrue(links.contains(link));
      if(link.isDirected()){
        links = manager.getConnections(ne2.getID(), ne1.getID(), link.isDirected(), Link.class);
        assertNotNull(links);
        assertTrue(links.size()>=0);
        assertFalse(links.contains(link));
      } else {
        links = manager.getConnections(ne2.getID(), ne1.getID(), link.isDirected(), Link.class);
        assertNotNull(links);
        assertTrue(links.size()>0);
        assertTrue(links.contains(link));
      }
    } catch (TopologyException e) {
      fail("Link could not be extracted from network element association");
    }

    log.info("Checking getConnections(ne1, ne2, isDirected,  layer, Class<Instance>)");
    try {
      links = manager.getConnections(ne1.getID(), ne2.getID(), link.isDirected(), link.getLayer(), Link.class);
      assertNotNull(links);
      assertTrue(!links.isEmpty());
      assertTrue(links.contains(link));
      if(link.isDirected()){
        links = manager.getConnections(ne2.getID(), ne1.getID(), link.isDirected(), link.getLayer(), Link.class);
        assertNotNull(links);
        assertTrue(links.size()>=0);
        assertFalse(links.contains(link));
      } else {
        links = manager.getConnections(ne2.getID(), ne1.getID(), link.isDirected(), link.getLayer(), Link.class);
        assertNotNull(links);
        assertTrue(links.size()>0);
        assertTrue(links.contains(link));
      }
    } catch (TopologyException e) {
      fail("Link could not be extracted from network element association");
    }
  }


  @Test
  public void testLinkCycle() {

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

    log.info("Attempting to create a new link");

    Link link = null;
    try {
      link = manager.createLink(cp1.getID(), cp2.getID());
    } catch (TopologyException e) {
      fail("Error when creating a link between the connection points");
    }

    assertNotNull(link);

    checkConnectionFunctions(manager, link, Link.class);

    checkConnectionAssociation(manager, link, ne1, Link.class);
    checkConnectionAssociation(manager, link, ne2, Link.class);
    link.setLayer(NetworkLayer.OTS);
    checkConnectionAssociation(manager, link, ne1, Link.class);
    checkConnectionAssociation(manager, link, ne2, Link.class);

    log.info("Checking if connection points have the association with the link");
    checkConnectionAssociation(manager, link, cp1, Link.class);
    checkConnectionAssociation(manager, link, cp2, Link.class);


    log.info("Checking if network elements have the association with the link");
    checkTopoManagerConnAssociation(manager, link, ne1, ne2);
    link.setDirected(true);
    checkTopoManagerDirectedConnectionAssociation(manager, link, ne1, ne2);
  }

  @Test
  public void testInvalidLink() {
    TopologyManager manager = getTopologyManager();
    NetworkElement ne1 = null;
    ConnectionPoint cp1 =null, cp2=null;
    Port cp3 =null, cp4=null;
    try {
      ne1 = manager.createNetworkElement();
      cp1 = manager.createConnectionPoint(ne1);
      cp2 = manager.createConnectionPoint(ne1);
      cp3 = manager.createPort(ne1);
      assertNotNull(cp3);
      cp4=manager.createPort(cp3);
    } catch (TopologyException e) {
      fail("Error when creating test.topology elements for creating a link");
    }

    assertNotNull(ne1);
    assertNotNull(cp1);
    assertNotNull(cp2);

    log.info("Attempting to create a link between two connection points in the same network element");
    Link link;
    try {
      link = manager.createLink(cp1.getID(), cp2.getID());
      fail("Link should not be created between two connection points in the same network element");
    } catch (TopologyException e) {
    }

    log.info("Attempting to create a link between two hierarchical connection points in the same network element");
    try {
      link = manager.createLink(cp3.getID(), cp4.getID());
      fail("Link should not be created between two hierarchical connection points in the same network element");
    } catch (TopologyException e) {
    }

    log.info("Attempting to create a link between two connection points with different parents in the same network element");
    try {
      link = manager.createLink(cp1.getID(), cp4.getID());
      fail("Link should not be created between two connection points with different parents in the same network element");
    } catch (TopologyException e) {
    }
  }
}
