package com.topology.impl.primitives;

import com.topology.primitives.ConnectionPoint;
import com.topology.primitives.Port;
import com.topology.primitives.TopologyElement;
import com.topology.primitives.TopologyManager;

import java.util.HashSet;
import java.util.Set;

public class PortImpl extends ConnectionPointImpl implements Port {

	private final  TopologyElement parent;

	private final Set<ConnectionPoint> cpSet;

	public PortImpl(TopologyManager manager, int id, TopologyElement parent) {
		super(manager, id);
		this.parent = parent;
		cpSet = new HashSet<>();
	}
	
	@Override
	public Set<ConnectionPoint> getContainedConnectionPoints() {
		return cpSet;
	}

	public void addContainedConnectionPoint(ConnectionPoint cp) {
		if (cp!=null) {
			cpSet.add(cp);
		}
	}

	public void removeContainedConnectionPoint(ConnectionPoint cp) {
		if (cp!=null) {
			if (cpSet.contains(cp)) {
				cpSet.remove(cp);
			}
		}
	}
	
	@Override
	public TopologyElement getParent() {
		return parent;
	}
}
