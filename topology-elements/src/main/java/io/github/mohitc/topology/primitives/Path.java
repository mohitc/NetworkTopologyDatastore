package io.github.mohitc.topology.primitives;

import java.util.List;

public interface Path {

	/**Boolean to indicate if path is a strict path from source to destination
	 * 
	 * @return true if path is strict else false
	 */
	boolean isStrict();
	
	/** Function to set if path is strict
	 * 
	 * @param strict boolean to indicate is path is a strict path
	 */
	void setStrict(boolean strict);
	
	/**Boolean to indicate if path is directed
	 * 
	 * @return true if directed else false
	 */
	boolean isDirected();
	
	/**Function to set if path is directed
	 * 
	 * @param directed boolean to indicate is path is directed
	 */
	void setDirected(boolean directed);
	
	/**Get the sequence of connections from the aEnd to the zEnd
	 * 
	 * @return ordered list of connections from the aEnd to the zEnd
	 */
	List<Connection> getForwardConnectionSequence();
	
	/**Function to set the sequence of forward connections
	 * 
	 * @param sequence
	 */
	void setForwardConnectionSequence(List<Connection> sequence);
	
	/**Function to get the list of connections from the zEnd to the aEnd
	 * 
	 * @return null if path is directed, else return a sqeuence of connections from the zEnd to the aEnd
	 */
	List<Connection> getBackwardConnectionSequence();
	
	/**Function to set the list of connections from the zEnd to the aEnd
	 * 
	 * @param sequence
	 */
	void setBackwardConnectionSequence(List<Connection> sequence);
	
	/** Function to get the a end of the path
	 * 
	 * @return aEnd connection point
	 */
	ConnectionPoint getaEnd();

	/**Function to set the aEnd of the path 
	 * 
	 * @param aEnd
	 */
	void setaEnd(ConnectionPoint aEnd);

	/**Function to get the zEnd of the path
	 * 
	 * @return zEnd connection point
	 */
	ConnectionPoint getzEnd();
	
	/**Function to set the zEnd of the path 
	 * 
	 * @param zEnd
	 */
	void setzEnd(ConnectionPoint zEnd);
	
}
