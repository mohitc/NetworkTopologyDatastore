package com.topology.primitives;

import java.util.Set;

import com.topology.primitives.exception.TopologyException;

public interface ConnectionPoint extends TopologyElement {
	//A connection point is a termination of connections in the network 
	//Functions in the connection point support the access of links associated with the connection point
	
	public Set<Connection> getConnections();
	
	public <T extends Connection> Set<T> getConnections(Class<T> instance);
	
	public Set<Connection> getConnections(NetworkLayer layer);

	public <T extends Connection> Set<T> getConnections(NetworkLayer layer, Class<T> instance);

	public TopologyElement getParent();
	
}
