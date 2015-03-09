package com.topology.impl.primitives;

import com.topology.primitives.ConnectionPoint;
import com.topology.primitives.Link;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.TopologyException;

public class LinkImpl extends ConnectionImpl implements Link {

	public LinkImpl(TopologyManager manager, int id, ConnectionPoint aEnd,
			ConnectionPoint zEnd) throws TopologyException {
		super(manager, id, aEnd, zEnd);
	}


}
