package com.topology.impl.primitives;

import com.helpers.notification.annotation.PropChange;
import com.topology.primitives.*;
import com.topology.primitives.exception.TopologyException;
import com.topology.primitives.resource.ConnectionResource;

public class TrailImpl extends PtpServiceImpl implements Trail {

	private Path path;

  private final ConnectionResource resource;

	public TrailImpl(TopologyManager manager, int id, ConnectionPoint aEnd, ConnectionPoint zEnd, boolean directed, NetworkLayer layer, Path path, ConnectionResource reservedResource) throws TopologyException {
		super(manager, id, aEnd, zEnd, directed, layer);
		this.path = path;
    this.resource = reservedResource;
	}

  @Override
  public ConnectionResource getReservedResource() {
    return resource;
  }

  @Override
  @PropChange
	public void setPath(Path path) {
		//TODO add code for validation
		this.path = path;
	}

	@Override
	public Path getPath() {
		return path;
	}

}
