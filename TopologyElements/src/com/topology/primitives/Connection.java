package com.topology.primitives;

import java.util.Map;

import com.topology.primitives.exception.resource.ResourceException;
import com.topology.primitives.resource.ConnectionResource;
import com.topology.primitives.resource.ConnectionResourceType;
import com.topology.primitives.exception.UnsupportedOperationException;

public interface Connection extends TopologyElement {

	public ConnectionPoint getaEnd();
	
	public ConnectionPoint getzEnd();

	public boolean isDirected();
	
	public void setDirected(boolean directed);
	
	public NetworkLayer getLayer();
	
	public void setLayer(NetworkLayer layer);
	
	public ConnectionResourceType getType();
	
	public ConnectionResource getTotalResources() throws ResourceException;
	
	public void setTotalResources(ConnectionResource resource) throws ResourceException, UnsupportedOperationException;
	
	public ConnectionResource getAvailableResources() throws ResourceException, UnsupportedOperationException;
	
	public ConnectionResource getReservedResources() throws ResourceException, UnsupportedOperationException;
	
	public Map<Integer, ConnectionResource> getReservations() throws ResourceException, UnsupportedOperationException;
	
	public boolean canReserve(ConnectionResource resource) throws ResourceException, UnsupportedOperationException;
	
	public void reserveConnection(int connID, ConnectionResource resource) throws ResourceException, UnsupportedOperationException;
	
	public void releaseConnection(int connID) throws ResourceException, UnsupportedOperationException;
	

}
