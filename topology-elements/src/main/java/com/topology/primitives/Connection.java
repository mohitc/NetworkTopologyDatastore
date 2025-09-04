package com.topology.primitives;

import com.topology.primitives.exception.UnsupportedOperationException;
import com.topology.primitives.exception.resource.ResourceException;
import com.topology.primitives.resource.ConnectionResource;

import java.util.Map;

public interface Connection extends TopologyElement {

	ConnectionPoint getaEnd();
	
	ConnectionPoint getzEnd();

	boolean isDirected();
	
	void setDirected(boolean directed);
	
	NetworkLayer getLayer();
	
	void setLayer(NetworkLayer layer);
	
	ConnectionResource getTotalResources() throws ResourceException;
	
	void setTotalResources(ConnectionResource resource) throws ResourceException, UnsupportedOperationException;
	
	ConnectionResource getAvailableResources() throws ResourceException, UnsupportedOperationException;
	
	ConnectionResource getReservedResources() throws ResourceException, UnsupportedOperationException;
	
	Map<Integer, ConnectionResource> getReservations() throws ResourceException, UnsupportedOperationException;
	
	boolean canReserve(ConnectionResource resource) throws ResourceException, UnsupportedOperationException;
	
	void reserveService(int connID, ConnectionResource resource) throws ResourceException, UnsupportedOperationException;
	
	void releaseService(int connID) throws ResourceException, UnsupportedOperationException;
	

}
