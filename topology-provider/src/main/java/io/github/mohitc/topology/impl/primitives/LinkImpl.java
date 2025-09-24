package io.github.mohitc.topology.impl.primitives;

import io.github.mohitc.topology.primitives.ConnectionPoint;
import io.github.mohitc.topology.primitives.Link;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.exception.TopologyException;

public class LinkImpl extends ConnectionImpl implements Link {

	public LinkImpl(TopologyManager manager, int id, ConnectionPoint aEnd,
			ConnectionPoint zEnd) throws TopologyException {
		super(manager, id, aEnd, zEnd);
	}


}
