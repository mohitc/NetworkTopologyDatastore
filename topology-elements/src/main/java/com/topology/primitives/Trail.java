package com.topology.primitives;

import com.topology.primitives.exception.TopologyException;
import com.topology.primitives.resource.ConnectionResource;

public interface Trail extends PtpService {

  /** Get the connection resource that has been reserved on all links on the trail
   *
   * @return resource
   */
  ConnectionResource getReservedResource();


  /**Function to set the path for the trail
	 * 
	 * @param path
	 * @throws TopologyException
	 */
	void setPath(Path path) throws TopologyException;
	
	/**Function to get the path for the trail
	 * 
	 * @return Path specified for the trail
	 */
	Path getPath();
}
