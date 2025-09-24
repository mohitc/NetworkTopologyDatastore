package io.github.mohitc.topology.primitives;

import java.util.Set;

public interface ConnectionPoint extends TopologyElement {
	//A connection point is a termination of connections in the network 
	//Functions in the connection point support the access of links associated with the connection point
	
	Set<Connection> getConnections();
	
	<T extends Connection> Set<T> getConnections(Class<T> instance);
	
	Set<Connection> getConnections(NetworkLayer layer);

	<T extends Connection> Set<T> getConnections(NetworkLayer layer, Class<T> instance);

	TopologyElement getParent();
	
}
