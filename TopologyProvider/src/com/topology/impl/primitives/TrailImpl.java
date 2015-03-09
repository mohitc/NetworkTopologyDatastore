package com.topology.impl.primitives;

import com.topology.primitives.ConnectionPoint;
import com.topology.primitives.Path;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.Trail;
import com.topology.primitives.exception.*;
import com.topology.primitives.exception.UnsupportedOperationException;
import com.topology.primitives.exception.resource.ResourceException;
import com.topology.primitives.resource.ConnectionResource;

import java.util.Map;

public class TrailImpl extends ConnectionImpl implements Trail {

	private Path path;

	public TrailImpl(TopologyManager manager, int id, ConnectionPoint aEnd, ConnectionPoint zEnd) throws TopologyException {
		super(manager, id, aEnd, zEnd);
	}

	public TrailImpl(TopologyManager manager, int id, ConnectionPoint aEnd, ConnectionPoint zEnd, Path path) throws TopologyException {
		super(manager, id, aEnd, zEnd);
		this.setPath(path); 
	}

	@Override
	public void setPath(Path path) {
		//TODO add code for validation
		this.path = path;
	}

	@Override
	public Path getPath() {
		return path;
	}

  @Override
  public ConnectionResource getAvailableResources() throws ResourceException,
    UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported for Trail type connections");
  }

  @Override
  public ConnectionResource getReservedResources() throws ResourceException,
    UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported for Trail type connections");
  }

  @Override
  public Map<Integer, ConnectionResource> getReservations()
    throws ResourceException, UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported for Trail type connections");
  }

  @Override
  public boolean canReserve(ConnectionResource resource)
    throws ResourceException, UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported for Trail type connections");
  }

  @Override
  public void reserveConnection(int connID, ConnectionResource resource)
    throws ResourceException, UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported for Trail type connections");
  }

  @Override
  public void releaseConnection(int connID) throws ResourceException,
    UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation not supported for Trail type connections");
  }

}
