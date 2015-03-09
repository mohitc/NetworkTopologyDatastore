package com.topology.primitives;

import java.util.Set;

import com.topology.primitives.exception.TopologyException;

public interface NetworkElement extends TopologyElement {
	
	/**Function to get all ports / connection points contained in the network element
	 * 
	 * @param iterate if false, only top level elements are returned, else complete hierarchy of connection points is provided
	 * @return
	 */
	public Set<ConnectionPoint> getConnectionPoints(boolean iterate);
	
	/**Function to get all connections at a specified network layer. 
	 * 
	 * @param layer (if null then all connections at all layers are provided)
	 * @return Set of all connections at a specified network layer
	 */
	public Set<Connection> getAllConnections();

	/**Function to get all connections at a specified network layer. 
	 * 
	 * @param layer (if null then all connections at all layers are provided)
	 * @return Set of all connections at a specified network layer
	 */
	public Set<Connection> getAllConnections(NetworkLayer layer);

	/**Function to get all connections of a specified type 
	 * 
	 * @param instance type of connection (link, cross connect, trail etc)
	 * @return Set of all connections of the specified type
	 */
	public <T extends Connection> Set<T> getAllConnections(Class<T> instance);

	/**Function to get all connections at a specified network layer of the type (instance)
	 * 
	 * @param layer  (if null then all connections at all layers are provided)
	 * @param instance Type of connections which are extracted from the set of all feasible connections
	 * @return
	 */
	public <T extends Connection> Set<T> getAllConnections(NetworkLayer layer, Class<T> instance);

}
