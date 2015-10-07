package com.topology.primitives;

import java.util.Set;

import com.topology.dto.PathDTO;
import com.topology.primitives.exception.TopologyException;
import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.TEPropertyKey;
import com.topology.primitives.properties.converters.PropertyConverter;
import com.topology.primitives.resource.ConnectionResource;

public interface TopologyManager {

  public String getIdentifier();

  public boolean hasElement(int id);

  public <T extends TopologyElement> boolean hasElement(int id, Class<T> instance);

  public <T extends TopologyElement> Set<T> getAllElements (Class<T> instance);

  public NetworkElement createNetworkElement () throws TopologyException;

  public ConnectionPoint createConnectionPoint (TopologyElement parent) throws TopologyException;

  public Port createPort (TopologyElement parent) throws TopologyException;

  public Link createLink (int startCpID, int endCpID) throws TopologyException;

  public CrossConnect createCrossConnect (int startCpID, int endCpID) throws TopologyException;

  public Trail createTrail (int startCpID, int endCpID, PathDTO pathDTO, boolean directed, ConnectionResource resource, NetworkLayer layer) throws TopologyException;

  public NetworkElement getNetworkElementFromCp(ConnectionPoint cp);

  //Functions to remove test.topology elements
  public void removeTopologyElement (int id) throws TopologyException;

  public void removeNetworkElement(int id) throws TopologyException;

  public void removeConnectionPoint(int id) throws TopologyException;

  public void removePort(int id) throws TopologyException;

  public void removeConnection(int id) throws TopologyException;

  public void removeLink(int id) throws TopologyException;

  public void removeCrossConnect(int id) throws TopologyException;

  public void removeTrail(int id) throws TopologyException;

  public void removeAllElements();

  //Function to get topology elements
  public TopologyElement getElementByID(int id) throws TopologyException;

  public <T extends TopologyElement> T getElementByID(int id, Class<T> instance) throws TopologyException;

  public <T extends TopologyElement> Set<T> getElementsByLabel(String label, Class<T> instance);

  public <T extends TopologyElement> T getSingleElementByLabel(String label, Class<T> instance) throws TopologyException;

  //Helper function to get all connections by layer
  public Set<Connection> getAllConnections (NetworkLayer layer) throws TopologyException;

  public <T extends Connection> Set<T> getAllConnections (Class<T> instance, NetworkLayer layer) throws TopologyException;

  //Helper functions to get connections between network elements
  public Set<Connection> getConnections (int startNeID, int endNeID) throws TopologyException;

  public <T extends Connection> Set<T> getConnections (int startNeID, int endNeID, Class<T> instance) throws TopologyException;

  public Set<Connection> getConnections (int startNeID, int endNeID, NetworkLayer layer) throws TopologyException;

  public <T extends Connection> Set<T> getConnections (int startNeID, int endNeID, NetworkLayer layer, Class<T> instance) throws TopologyException;

  public Set<Connection> getConnections (int startNeID, int endNeID, boolean isDirected) throws TopologyException;

  public <T extends Connection> Set<T> getConnections (int startNeID, int endNeID, boolean isDirected, Class<T> instance) throws TopologyException;

  public Set<Connection> getConnections (int startNeID, int endNeID, boolean isDirected, NetworkLayer layer) throws TopologyException;

  public <T extends Connection> Set<T> getConnections (int startNeID, int endNeID, boolean isDirected, NetworkLayer layer, Class<T> instance) throws TopologyException;

  // Methods to register and access TE Property keys associated with a topology Manager

  public TEPropertyKey registerKey(String id, String desc, Class objectClass, Class<? extends PropertyConverter> converterClass) throws PropertyException;

  public boolean containsKey(String id);

  public boolean containsKey(TEPropertyKey key);

  public TEPropertyKey getKey(String id) throws PropertyException;

  public void removeKey(String id) throws PropertyException;

  public void removeKey(TEPropertyKey key) throws PropertyException;

  //Methods to add/remove properties associated to the Topology Manager itself
  /**Get a property associated with the key of the object
   *
   * @param key
   * @return associated value
   * @throws PropertyException when key is not found
   */
  public Object getProperty(TEPropertyKey key) throws PropertyException;

  /**Get a property associated with the Key of the object, if of type K
   *
   * @param key
   * @param instance class of which the associated value should be mapped
   * @return	associated value
   * @throws PropertyException if key is not found, or is not of type K
   */
  public <K> K getProperty(TEPropertyKey key, Class<K> instance) throws PropertyException;

  /**Boolean to check if property is set in the test.topology element
   *
   * @param key
   * @return true if property with key "key" has been assigned to the map
   */
  public boolean hasProperty(TEPropertyKey key);

  /**Function to add a property to the test.topology element
   *
   * @param key
   * @param value
   */
  public void addProperty(TEPropertyKey key, Object value) throws PropertyException;

  /**Method to remove a property associated with the network element
   *
   * @param key
   * @throws PropertyException
   */
  public void removeProperty(TEPropertyKey key) throws PropertyException;

  /**Function to get the set of all properties defined for the test.topology element
   *
   * @return Set of TEPropertyKey values
   */
  public Set<TEPropertyKey> definedPropertyKeys();


}

