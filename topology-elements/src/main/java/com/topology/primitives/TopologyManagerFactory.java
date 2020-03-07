
package com.topology.primitives;

import com.topology.primitives.exception.TopologyException;

public interface TopologyManagerFactory {

  /**Function to create a topology manager instance identified by a unique string
   *
   * @param id unique string identifier for the topology manager (lower case string used)
   * @return
   * @throws TopologyException if ID is not unique
   */
  TopologyManager createTopologyManager(String id) throws TopologyException;

  /**Function to get a topology manager instance identified by the id string
   *
   * @param id
   * @return
   * @throws TopologyException is manager is not found
   */
  TopologyManager getTopologyManager(String id) throws TopologyException;

  /**Boolean function to indicate if topology manager instance with specified ID exists in the map
   *
   * @param id
   * @return
   */
  boolean hasTopologyManager(String id);

  /**Function to delete a topology manager instance identified by a unique string
   *
   * @param id unique string identifier for the topology manager (lower case string used)
   * @throws TopologyException if ID is not unique
   */
  void removeTopologyManager(String id) throws TopologyException;
}
