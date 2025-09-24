package io.github.mohitc.topology.impl.primitives;

import io.github.mohitc.topology.primitives.Connection;
import io.github.mohitc.topology.primitives.ConnectionPoint;
import io.github.mohitc.topology.primitives.NetworkLayer;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.connresource.DefaultConnectionResource;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import io.github.mohitc.topology.primitives.exception.UnsupportedOperationException;
import io.github.mohitc.topology.primitives.exception.resource.ResourceException;
import io.github.mohitc.topology.primitives.resource.ConnectionResource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ConnectionImpl extends TopologyElementImpl implements Connection  {

	public ConnectionImpl(TopologyManager manager, int id, ConnectionPoint aEnd, ConnectionPoint zEnd) throws TopologyException {
		super(manager, id);
		this.aEnd = aEnd;
		this.zEnd = zEnd;
	}

	private final ConnectionPoint aEnd;

  private final ConnectionPoint zEnd;

	private boolean directed;

	private NetworkLayer layer;

  //Total resources assigned to a connection
  private ConnectionResource totalResource = new DefaultConnectionResource();

  //Map to store the reservations against a connection(
  private Map<Integer, ConnectionResource> reservations;

	@Override
	public ConnectionPoint getzEnd() {
		return zEnd;
	}

	@Override
	public ConnectionPoint getaEnd() {
		return aEnd;
	}

	@Override
	public boolean isDirected() {
		return directed;
	}

	@Override
	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	@Override
	public NetworkLayer getLayer() {
		return layer;
	}

	@Override
	public void setLayer(NetworkLayer layer) {
		this.layer = layer;
	}

	@Override
	public ConnectionResource getTotalResources() throws ResourceException {
		return totalResource;
	}

	@Override
	public void setTotalResources(ConnectionResource resource)
			throws ResourceException, UnsupportedOperationException {
		if (resource==null) {
      throw new ResourceException("Total resources cannot be null");
    }
    //Check against reservations when setting total resources
    if (reservations != null) {
      Set<ConnectionResource> reservedResources = new HashSet<>(reservations.values());
      resource.availableResource(reservedResources);
      //If available resources were found, the reservation is possible,
    }
    this.totalResource = resource;
  }

	@Override
	public ConnectionResource getAvailableResources() throws ResourceException{
    if (reservations==null) {
      return this.totalResource;
    } else{
			Set<ConnectionResource> reservedResources = new HashSet<>(reservations.values());
      return totalResource.availableResource(reservedResources);
    }
	}

	@Override
	public ConnectionResource getReservedResources() throws ResourceException{
    ConnectionResource reservedResource = null;
    for(Map.Entry<Integer, ConnectionResource> entry: reservations.entrySet()) {
      if (reservedResource == null) {
        reservedResource = entry.getValue();
      } else {
        reservedResource = reservedResource.join(entry.getValue());
      }
    }
		return null;
	}

	@Override
	public Map<Integer, ConnectionResource> getReservations()
			throws ResourceException {
		return Collections.unmodifiableMap(reservations);
	}

	@Override
	public boolean canReserve(ConnectionResource resource)
			throws ResourceException {
    if (resource==null) {
      throw new ResourceException("Check for reservation against a resource cannot be null");
    }
    try {
      ConnectionResource availableResource = this.getAvailableResources();
      //Check if new resource can be reserved on the available resource
      availableResource.availableResource(resource);
      //no exception thrown i.e. reservation is possible
      return true;
    } catch (ResourceException e) {
      //Exception while computing available resource implying that reservation is not possible
      return false;
    }
	}

	@Override
	public void reserveService(int connID, ConnectionResource resource)
			throws ResourceException {
    if (canReserve(resource)) {
      //check if a reservation with this id already exists
      if (reservations.containsKey(connID)) {
        throw new ResourceException("A reservation for connection with ID: " + connID + " already exists. Please release this resource before making another reservation");
      }
      reservations.put(connID, resource);
    }
	}

	@Override
	public void releaseService(int connID) throws ResourceException {
    if (reservations.containsKey(connID)) {
      reservations.remove(connID);
    } else {
      throw new ResourceException("No reservation found for connection with ID: " + connID);
    }
	}
}
