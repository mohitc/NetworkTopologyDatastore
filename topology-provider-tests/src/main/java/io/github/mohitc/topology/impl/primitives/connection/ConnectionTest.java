package io.github.mohitc.topology.impl.primitives.connection;

import io.github.mohitc.topology.primitives.*;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import io.github.mohitc.topology.test.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ConnectionTest implements TestCase {

  protected static final Logger log = LoggerFactory.getLogger(ConnectionTest.class);

  protected final TopologyManager manager;

  public ConnectionTest(TopologyManager instance) {
    this.manager = instance;
  }

  protected <T extends Connection> void checkConnectionFunctions(TopologyManager manager, T conn, Class<T> instance) {
    log.info("Checking to see if the test.topology manager has the associated link");

    try {
      TopologyElement newConn = manager.getElementByID(conn.getID());
      if (newConn==null) {
        fail("Connection in the test.topology manager is null");
      }
      if (instance.isAssignableFrom(newConn.getClass())) {
        assertEquals(conn, newConn);
      } else {
        fail("Invalid class assignment for connection");
      }
    } catch (TopologyException e) {
      fail("Problems with connection associations in the test.topology manager");
    }

    try {
      T newConn = manager.getElementByID(conn.getID(), instance);
      if (newConn==null) {
        fail("Connection in the test.topology manager is null");
      }
      assertEquals(conn, newConn);
    } catch (TopologyException e) {
      fail("Problems with connection associations in the test.topology manager");
    }
  }

  protected <T extends Connection> void checkConnectionAssociation(TopologyManager manager, T conn, ConnectionPoint cp, Class<T> instance) {
    if (!(conn.getaEnd().equals(cp) || conn.getzEnd().equals(cp))) {
      fail("Connection point " + cp + " is not an endpoint of connection " + conn);
    }
    log.info("Checking if connections associated with {} contain connection {}", cp, conn);
    assertTrue(cp.getConnections().contains(conn));
    assertTrue(cp.getConnections(instance).contains(conn));
    if (conn.getLayer()!=null) {
      assertTrue(cp.getConnections(conn.getLayer()).contains(conn));
      assertTrue(cp.getConnections(conn.getLayer(), instance).contains(conn));
    }
  }

  protected <T extends Connection> void checkConnectionAssociation(TopologyManager manager, T conn, NetworkElement ne, Class<T> instance) {
    Set<Connection> connections;
    log.info("Checking network element connection associations");

    log.info("Checking ne.getAllConnections()");
    connections = ne.getAllConnections();
    assertNotNull(connections);
    assertFalse(connections.isEmpty());
    assertTrue(connections.contains(conn));

    if (conn.getLayer()!=null) {
      log.info("Checking ne.getAllConnections(layer)");
      connections = ne.getAllConnections(conn.getLayer());
      assertNotNull(connections);
      assertFalse(connections.isEmpty());
      assertTrue(connections.contains(conn));
    }

    log.info("Checking ne.getAllConnections(Class<T> instance)");
    Set<T> classConnections = ne.getAllConnections(instance);
    assertNotNull(classConnections);
    assertFalse(classConnections.isEmpty());
    assertTrue(classConnections.contains(conn));

    if (conn.getLayer()!=null) {
      log.info("Checking ne.getAllConnections(NetworkLayer, Class<T> instance)");
      classConnections = ne.getAllConnections(conn.getLayer(), instance);
      assertNotNull(classConnections);
      assertFalse(classConnections.isEmpty());
      assertTrue(classConnections.contains(conn));
    }

    if (conn.getLayer()!=null){
      log.info("Checking list of all connections in Topology Manager, filtered by Layer");
      try {
        connections = manager.getAllConnections(conn.getLayer());
        assertNotNull(connections);
        assertFalse(connections.isEmpty());
        assertTrue(connections.contains(conn));
      } catch (Exception e) {
        log.error("Exception while checking connection in topology manager", e);
        fail (e.getMessage());
      }
    }

    log.info("Checking list of all connections in Topology Manager, filtered by Layer and class type");
    if (conn.getLayer()!=null){
      try {
        classConnections = manager.getAllConnections(instance, conn.getLayer());
        assertNotNull(classConnections);
        assertFalse(classConnections.isEmpty());
        assertTrue(classConnections.contains(conn));
      } catch (Exception e) {
        log.error("Exception while checking connection in topology manager", e);
        fail (e.getMessage());
      }
    }
  }
}
