package com.topology.primitives;

import java.util.Set;

public interface Port extends ConnectionPoint {

	/** Function to get the connection points contained in a port
	 * 
	 * @return Set of connection points contained in a port
	 */
	Set<ConnectionPoint> getContainedConnectionPoints();
	
}
