package io.github.mohitc.topology.impl.primitives;

import io.github.mohitc.topology.primitives.ConnectionPoint;
import io.github.mohitc.topology.primitives.Port;
import io.github.mohitc.topology.primitives.TopologyElement;
import io.github.mohitc.topology.primitives.TopologyManager;

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
      cpSet.remove(cp);
		}
	}

	@Override
	public TopologyElement getParent() {
		return parent;
	}
}
