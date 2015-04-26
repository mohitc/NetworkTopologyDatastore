package com.topology.primitives;

import java.util.Set;

import com.topology.dto.PathDTO;
import com.topology.primitives.exception.TopologyException;
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

}

