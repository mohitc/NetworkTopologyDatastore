package com.topology.impl.primitives;

import com.topology.primitives.ConnectionPoint;
import com.topology.primitives.CrossConnect;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.TopologyException;

public class CrossConnectImpl extends ConnectionImpl implements CrossConnect{

	public CrossConnectImpl(TopologyManager manager,int id,
			ConnectionPoint aEnd, ConnectionPoint zEnd)
			throws TopologyException {
		super(manager, id, aEnd, zEnd);
	}

}
