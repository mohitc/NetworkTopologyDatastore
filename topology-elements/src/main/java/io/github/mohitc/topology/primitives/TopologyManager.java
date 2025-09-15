package io.github.mohitc.topology.primitives;

import io.github.mohitc.topology.dto.PathDTO;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import io.github.mohitc.topology.primitives.exception.properties.PropertyException;
import io.github.mohitc.topology.primitives.properties.TEPropertyKey;
import io.github.mohitc.topology.primitives.properties.converters.PropertyConverter;
import io.github.mohitc.topology.primitives.resource.ConnectionResource;

import java.util.Set;

public interface TopologyManager {

  String getIdentifier();

  boolean hasElement(int id);

  <T extends TopologyElement> boolean hasElement(int id, Class<T> instance);

  <T extends TopologyElement> Set<T> getAllElements (Class<T> instance);

  NetworkElement createNetworkElement () throws TopologyException;

  ConnectionPoint createConnectionPoint (TopologyElement parent) throws TopologyException;

  Port createPort (TopologyElement parent) throws TopologyException;

  Link createLink (int startCpID, int endCpID) throws TopologyException;

  CrossConnect createCrossConnect (int startCpID, int endCpID) throws TopologyException;

  Trail createTrail (int startCpID, int endCpID, PathDTO pathDTO, boolean directed, ConnectionResource resource, NetworkLayer layer) throws TopologyException;

  NetworkElement getNetworkElementFromCp(ConnectionPoint cp);

  //Functions to remove test.topology elements
  void removeTopologyElement (int id) throws TopologyException;

  void removeNetworkElement(int id) throws TopologyException;

  void removeConnectionPoint(int id) throws TopologyException;

  void removePort(int id) throws TopologyException;

  void removeConnection(int id) throws TopologyException;

  void removeLink(int id) throws TopologyException;

  void removeCrossConnect(int id) throws TopologyException;

  void removeTrail(int id) throws TopologyException;

  void removeAllElements();

  //Function to get topology elements
  TopologyElement getElementByID(int id) throws TopologyException;

  <T extends TopologyElement> T getElementByID(int id, Class<T> instance) throws TopologyException;

  <T extends TopologyElement> Set<T> getElementsByLabel(String label, Class<T> instance);

  <T extends TopologyElement> T getSingleElementByLabel(String label, Class<T> instance) throws TopologyException;

  //Helper function to get all connections by layer
  Set<Connection> getAllConnections (NetworkLayer layer) throws TopologyException;

  <T extends Connection> Set<T> getAllConnections (Class<T> instance, NetworkLayer layer) throws TopologyException;

  //Helper functions to get connections between network elements
  Set<Connection> getConnections (int startNeID, int endNeID) throws TopologyException;

  <T extends Connection> Set<T> getConnections (int startNeID, int endNeID, Class<T> instance) throws TopologyException;

  Set<Connection> getConnections (int startNeID, int endNeID, NetworkLayer layer) throws TopologyException;

  <T extends Connection> Set<T> getConnections (int startNeID, int endNeID, NetworkLayer layer, Class<T> instance) throws TopologyException;

  Set<Connection> getConnections (int startNeID, int endNeID, boolean isDirected) throws TopologyException;

  <T extends Connection> Set<T> getConnections (int startNeID, int endNeID, boolean isDirected, Class<T> instance) throws TopologyException;

  Set<Connection> getConnections (int startNeID, int endNeID, boolean isDirected, NetworkLayer layer) throws TopologyException;

  <T extends Connection> Set<T> getConnections (int startNeID, int endNeID, boolean isDirected, NetworkLayer layer, Class<T> instance) throws TopologyException;

  // Methods to register and access TE Property keys associated with a topology Manager

  TEPropertyKey registerKey(String id, String desc, Class objectClass, Class<? extends PropertyConverter> converterClass) throws PropertyException;

  boolean containsKey(String id);

  boolean containsKey(TEPropertyKey key);

  TEPropertyKey getKey(String id) throws PropertyException;

  void removeKey(String id) throws PropertyException;

  void removeKey(TEPropertyKey key) throws PropertyException;

  //Methods to add/remove properties associated to the Topology Manager itself
  /**Get a property associated with the key of the object
   *
   * @param key
   * @return associated value
   * @throws PropertyException when key is not found
   */
  Object getProperty(TEPropertyKey key) throws PropertyException;

  /**Get a property associated with the Key of the object, if of type K
   *
   * @param key
   * @param instance class of which the associated value should be mapped
   * @return	associated value
   * @throws PropertyException if key is not found, or is not of type K
   */
  <K> K getProperty(TEPropertyKey key, Class<K> instance) throws PropertyException;

  /**Boolean to check if property is set in the test.topology element
   *
   * @param key
   * @return true if property with key "key" has been assigned to the map
   */
  boolean hasProperty(TEPropertyKey key);

  /**Function to add a property to the test.topology element
   *
   * @param key
   * @param value
   */
  void addProperty(TEPropertyKey key, Object value) throws PropertyException;

  /**Method to remove a property associated with the network element
   *
   * @param key
   * @throws PropertyException
   */
  void removeProperty(TEPropertyKey key) throws PropertyException;

  /**Function to get the set of all properties defined for the test.topology element
   *
   * @return Set of TEPropertyKey values
   */
  Set<TEPropertyKey> definedPropertyKeys();


}

