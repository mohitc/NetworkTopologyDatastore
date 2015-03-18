package test.topology.primitives.connection;

import com.topology.primitives.*;
import com.topology.primitives.exception.TopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.topology.primitives.manager.TopoManagerHelper;

import java.util.Set;

import static org.junit.Assert.*;

public class ConnectionTest {

    protected static Logger log = LoggerFactory.getLogger(ConnectionTest.class);

    protected TopologyManager getTopologyManager() {
        return TopoManagerHelper.getInstance();
    }

    protected <T extends Connection> void checkConnectionFunctions(TopologyManager manager, T conn, Class<T> instance) {
        log.info("Checking to see if the test.topology manager has the associated link");

        try {
            TopologyElement newConn = manager.getElementByID(conn.getID());
            if (newConn==null) {
                fail("Connection in the test.topology manager is null");
            }
            if (instance.isAssignableFrom(newConn.getClass())) {
                assertTrue(conn.equals(newConn));
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
            assertTrue(conn.equals(newConn));
        } catch (TopologyException e) {
            fail("Problems with connection associations in the test.topology manager");
        }
    }

    protected <T extends Connection> void checkConnectionAssociation(TopologyManager manager, T conn, ConnectionPoint cp, Class<T> instance) {
        if (!(conn.getaEnd().equals(cp) || conn.getzEnd().equals(cp))) {
            fail("Connection point " + cp + " is not an endpoint of connection " + conn);
        }
        log.info("Checking if connections associated with " + cp + " contain connection " + conn);
        assertTrue(cp.getConnections().contains(conn));
        assertTrue(cp.getConnections(instance).contains(conn));
        if (conn.getLayer()!=null) {
            assertTrue(cp.getConnections(conn.getLayer()).contains(conn));
            assertTrue(cp.getConnections(conn.getLayer(), instance).contains(conn));
        }
    }

    protected <T extends Connection> void checkConnectionAssociation(TopologyManager manager, T conn, NetworkElement ne, Class<T> instance) {
        Set<Connection> connections = null;
        log.info("Checking network element connection associations");

        log.info("Checking ne.getAllConnections()");
        connections = ne.getAllConnections();
        assertNotNull(connections);
        assertTrue(connections.size()>0);
        assertTrue(connections.contains(conn));

        if (conn.getLayer()!=null) {
            connections = null;
            log.info("Checking ne.getAllConnections(layer)");
            connections = ne.getAllConnections(conn.getLayer());
            assertNotNull(connections);
            assertTrue(connections.size() > 0);
            assertTrue(connections.contains(conn));
        }

        log.info("Checking ne.getAllConnections(Class<T> instance)");
        Set<T> classConnections = ne.getAllConnections(instance);
        assertNotNull(classConnections);
        assertTrue(classConnections.size() > 0);
        assertTrue(classConnections.contains(conn));

        if (conn.getLayer()!=null) {
            log.info("Checking ne.getAllConnections(NetworkLayer, Class<T> instance)");
            classConnections = ne.getAllConnections(conn.getLayer(), instance);
            assertNotNull(classConnections);
            assertTrue(classConnections.size() > 0);
            assertTrue(classConnections.contains(conn));
        }

      if (conn.getLayer()!=null){
        log.info("Checking list of all connections in Topology Manager, filtered by Layer");
        try {
          connections = manager.getAllConnections(conn.getLayer());
          assertNotNull(connections);
          assertTrue(connections.size() > 0);
          assertTrue(connections.contains(conn));
        } catch (TopologyException e) {
          fail (e.getMessage());
        }
      }

      log.info("Checking list of all connections in Topology Manager, filtered by Layer and class type");
      if (conn.getLayer()!=null){
        try {
          classConnections = manager.getAllConnections(instance, conn.getLayer());
          assertNotNull(classConnections);
          assertTrue(classConnections.size() > 0);
          assertTrue(classConnections.contains(conn));
        } catch (TopologyException e) {
          fail (e.getMessage());
        }
      }

    }
}
