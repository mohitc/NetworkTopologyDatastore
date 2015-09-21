package com.topology.primitives;

import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.TEPropertyKey;

import java.util.Set;


public interface TopologyElement {

  /**Function to get the topology manager responsible for this network element
   *
   * @return Instance of the topology manager
   */
  public TopologyManager getTopologyManager();

	/**Unique identifier for the test.topology element
	 * 
	 * @return Unique Integer identifier
	 */
	public int getID();
	
	/**Short label for the test.topology element
	 * 
	 * @return String with the label
	 */
	public String getLabel();
	
	/**Function to set the label for the test.topology element
	 * 
	 * @param label
	 */
	public void setLabel(String label);
	
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
