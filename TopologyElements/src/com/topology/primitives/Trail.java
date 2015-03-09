package com.topology.primitives;

import com.topology.primitives.exception.TopologyException;

public interface Trail extends Connection {

	/**Function to set the path for the trail
	 * 
	 * @param path
	 * @throws TopologyException
	 */
	public void setPath(Path path) throws TopologyException;
	
	/**Function to get the path for the trail
	 * 
	 * @return Path specified for the trail
	 */
	public Path getPath();
}
