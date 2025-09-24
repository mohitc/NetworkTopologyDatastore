package io.github.mohitc.topology.impl.primitives.networkelement;

import io.github.mohitc.topology.impl.primitives.manager.TopoManagerHelper;
import io.github.mohitc.topology.primitives.*;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkElementTest {

	private static final Logger log = LoggerFactory.getLogger(NetworkElementTest.class);


	private TopologyManager getTopologyManager() {
		return TopoManagerHelper.getInstance();
	}

	//----------------------------------------------------------------------------------------------------------------------------
	//Test and related functions for the lifecycle of network elements
	//----------------------------------------------------------------------------------------------------------------------------
	public NetworkElement createNetworkElement(TopologyManager manager){
		log.info("Creating a new Network element from the test.topology manager");

		NetworkElement ne=null;
		try {
			ne = manager.createNetworkElement();
			ne.setLabel("Test NE");
		} catch (TopologyException e) {
			log.error("Error in creating a network element", e);
		}
		if(ne==null) {
			fail("Network element was not created successfully");
		}
		return ne;
	}

	private void checkNetworkElement(NetworkElement ne, TopologyManager manager) {
		//check if the test.topology manager has the network element
		log.info("Checking if manager has the specified network element");
		assertTrue(manager.hasElement(ne.getID()));
		assertTrue(manager.hasElement(ne.getID(), NetworkElement.class));

		//get the network element from the test.topology manager
		NetworkElement createdNe = null;
		try {
			createdNe = manager.getElementByID(ne.getID(), NetworkElement.class);
		} catch (TopologyException e) {
			log.error("Error when trying to fetch the network element from the test.topology manager: ", e);
		}
		if (createdNe==null) {
			fail("Could not get created network element from manager");
		}

		//compare the created network element with the generated network element
    assertEquals(ne, createdNe);
	}


	@Test
	public void testEmptyNetworkElementLifeCycle(){
		log.info("Initializing Network Manager");
		TopologyManager manager = getTopologyManager();
		//generating a new network element
		NetworkElement ne = createNetworkElement(manager);

		//Check the existence of the network element in the test.topology manager
		checkNetworkElement(ne, manager);

		log.info("Attempting to delete the created network element");
		//attempting to delete the network element while the network element has a port should throw an exception
		try {
			manager.removeNetworkElement(ne.getID());
		} catch (TopologyException e) {
			log.error("Error when removing network element", e);
		}

    assertFalse(manager.hasElement(ne.getID()));
    assertFalse(manager.hasElement(ne.getID(), NetworkElement.class));
	}


	//----------------------------------------------------------------------------------------------------------------------------
	//Test and related functions for the lifecycle of ports
	//----------------------------------------------------------------------------------------------------------------------------

	private Port createPort(TopologyElement parent, TopologyManager manager) {
		Port port = null;
		try {
			port = manager.createPort(parent);
		} catch (TopologyException e) {
			log.error("Error in creating a port in the test.topology element", e);
		}
		if (port==null) {
			fail("Port not created successfully");
		}
		return port;
	}

	private ConnectionPoint createConnectionPoint(TopologyElement parent, TopologyManager manager) {
		ConnectionPoint cp = null;
		try {
			cp = manager.createConnectionPoint(parent);
		} catch (TopologyException e) {
			log.error("Error in creating a connection point in the test.topology element", e);
		}
		if (cp==null) {
			fail("Connection Point not created successfully");
		}
		return cp;
	}

	private <T extends ConnectionPoint> void checkConnectionPoint(ConnectionPoint port, TopologyElement parent, TopologyManager manager, Class<T> instance) {

		//check if the test.topology manager has the network element
		log.info("Checking if manager has the specified Port");
		assertTrue(manager.hasElement(port.getID()));
		assertTrue(manager.hasElement(port.getID(), instance));

		//get the network element from the test.topology manager
		T createdPort = null;
		try {
			createdPort = manager.getElementByID(port.getID(), instance);
		} catch (TopologyException e) {
			log.error("Error when trying to fetch the connection point from the test.topology manager: ", e);
		}
		if (createdPort==null) {
			fail("Could not get created test.topology element from manager");
		}

		//compare the created network element with the generated network element
    assertEquals(port, createdPort);

		//if parent is a network element, check if network element contains the port
		if (NetworkElement.class.isAssignableFrom(parent.getClass())) {
			//get the parent network element from the test.topology manager
			NetworkElement ne = null;
			try {
				ne = manager.getElementByID(parent.getID(), NetworkElement.class);
			} catch (TopologyException e) {
				log.error("Error in fetching the network element from the test.topology manager", e);
			}
			if (ne==null){
				fail("Could not fetch network element from test.topology manager");
			}
      log.info("Set of connection points in network element{}", ne.getConnectionPoints(false));
			assertTrue(ne.getConnectionPoints(false).contains(port));
		} else if (Port.class.isAssignableFrom(parent.getClass())) {
			//get the parent network element from the test.topology manager
			Port parentPort = null;
			try {
				parentPort = manager.getElementByID(parent.getID(), Port.class);
			} catch (TopologyException e) {
				log.error("Error in fetching the parent port from the test.topology manager", e);
			}
			if (parentPort==null){
				fail("Could not fetch parent port from test.topology manager");
			}
		} else {
			fail("Invalid parent for the connection point");
		}
	}



	public <T extends ConnectionPoint> void testPopulatedNetworkElementLifeCycle(Class<T> instance){
		log.info("Initializing Network Manager");
		TopologyManager manager = getTopologyManager();
		//generating a new network element
		NetworkElement ne = createNetworkElement(manager);

		//Check the existence of the network element in the test.topology manager
		checkNetworkElement(ne, manager);

		log.info("Create a port in the network element");

		ConnectionPoint port;
		if (Port.class.isAssignableFrom(instance))
			port = createPort(ne, manager);
		else
			port = createConnectionPoint(ne, manager);

		checkConnectionPoint(port, ne, manager, instance);

		//get the port from the network test.topology
		log.info("Attempting to delete the created network element without cleanup");
		try {
			manager.removeNetworkElement(ne.getID());
			//Exception should be thrown
			fail("Network element should not be deleted without cleanup");
		} catch (TopologyException e) {
			log.info("Network element could not be deleted without cleanup");
		}

		//delete port
		log.info("Attempting to delete the created port");
		try {
			manager.removePort(port.getID());
		} catch (TopologyException e) {
			log.error("Error while deleting created port: ", e);
		}

		log.info("Checking if port was deleted successfully");
    assertFalse(manager.hasElement(port.getID()));
    assertFalse(manager.hasElement(port.getID(), instance));
		assertFalse(ne.getConnectionPoints(false).contains(port));


		//delete network element
		try {
			manager.removeNetworkElement(ne.getID());
		} catch (TopologyException e) {
			log.error("Error while deleting created network element: ", e);
		}

    assertFalse(manager.hasElement(ne.getID()));
    assertFalse(manager.hasElement(ne.getID(), NetworkElement.class));

	}

	@Test
	public void testPopulatedNe(){
		testPopulatedNetworkElementLifeCycle(Port.class);
		testPopulatedNetworkElementLifeCycle(ConnectionPoint.class);
	}

	public <T extends ConnectionPoint> void testHierarchicalPortStructure(Class<T> instance){
		log.info("Initializing Network Manager");
		TopologyManager manager = getTopologyManager();
		//generating a new network element
		NetworkElement ne = createNetworkElement(manager);

		log.info("Create a port in the network element");
		Port port = createPort(ne, manager);

		log.info("Create a connection point in the port");
		ConnectionPoint childPort;
		if (Port.class.isAssignableFrom(instance))
			childPort = createPort(port, manager);
		else
			childPort = createConnectionPoint(port, manager);

		checkConnectionPoint(childPort, port, manager, instance);

		//Check if iterative scanning of ports in the network element is working
		assertTrue(ne.getConnectionPoints(true).contains(port));

		log.info("Attempting to delete the created network element without cleanup");
		try {
			manager.removeNetworkElement(ne.getID());
			//Exception should be thrown
			fail("Network element should not be deleted without cleanup");
		} catch (TopologyException e) {
			assertFalse(false);
		}

		//deleting parent port without deleting contained ports should throw an exception
		log.info("Attempting to delete the parent port without deleting the child port");
		try {
			manager.removePort(port.getID());
			fail("Parent Port should not be deleted before cleanup");
		} catch (TopologyException e) {
			log.info("Parent port could not be deleted without deleting the child port");
		}

		//cleanup
		log.info("Cleaning up test.topology manager");
		try {
			manager.removePort(childPort.getID());
		} catch (TopologyException e) {
			log.info("Exception when cleaning up ports");
		}
    assertFalse(manager.hasElement(childPort.getID()));
    assertFalse(manager.hasElement(childPort.getID(), instance));
		assertFalse(ne.getConnectionPoints(true).contains(childPort));

		try {
			manager.removePort(port.getID());
		} catch (TopologyException e) {
			log.info("Exception when cleaning up ports");
		}

    assertFalse(manager.hasElement(port.getID()));
    assertFalse(manager.hasElement(port.getID(), Port.class));
		assertFalse(ne.getConnectionPoints(false).contains(port));

		//delete network element
		try {
			manager.removeNetworkElement(ne.getID());
		} catch (TopologyException e) {
			log.error("Error while deleting created network element: ", e);
		}

    assertFalse(manager.hasElement(ne.getID()));
    assertFalse(manager.hasElement(ne.getID(), NetworkElement.class));

	}

	@Test
	public void testHierarchicalPort(){
//      InitNotificationManager.init();
		testHierarchicalPortStructure(Port.class);
		testHierarchicalPortStructure(ConnectionPoint.class);
	}


}
