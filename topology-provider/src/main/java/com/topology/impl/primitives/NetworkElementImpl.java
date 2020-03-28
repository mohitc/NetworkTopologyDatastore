package com.topology.impl.primitives;

import java.util.HashSet;
import java.util.Set;

import com.topology.primitives.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topology.primitives.exception.TopologyException;

public class NetworkElementImpl extends TopologyElementImpl implements NetworkElement {

	private static final Logger log = LoggerFactory.getLogger(NetworkElementImpl.class);
	
	private Set<ConnectionPoint> cpSet;

	public NetworkElementImpl(TopologyManager manager, int id){
		super(manager, id);
		cpSet = new HashSet<>();
	}
	
	private Set<ConnectionPoint> containedConnectionPoints (ConnectionPoint cp) {
		Set<ConnectionPoint> cps = new HashSet<>();
		cps.add(cp);
		if (Port.class.isAssignableFrom(cp.getClass())) {
			for (ConnectionPoint temp: ((Port)cp).getContainedConnectionPoints()) {
				cps.addAll(containedConnectionPoints(temp));
			}
		}
		return cps;
	}
	
	@Override
	public Set<ConnectionPoint> getConnectionPoints(boolean iterate) {
		if (!iterate)
			return cpSet;
		else {
			Set<ConnectionPoint> allCps = new HashSet<>();
			for (ConnectionPoint cp: cpSet) {
				allCps.addAll(containedConnectionPoints(cp));
			}
			return allCps;
		}
	}

	@Override
	public Set<Connection> getAllConnections() {
		Set<ConnectionPoint> cps = getConnectionPoints(true);
		Set<Connection> connections = new HashSet<>();
		for (ConnectionPoint cp: cps) {
			connections.addAll(cp.getConnections());
		}
		return connections;
	}

	@Override
	public Set<Connection> getAllConnections(NetworkLayer layer) {
		Set<Connection> connections = getAllConnections();
		if (layer==null) {
			log.info("Layer is null, returning all connections");
			return connections;
		}
		Set<Connection> outSet = new HashSet<>();
		for (Connection conn: connections) {
			if (conn.getLayer().equals(layer)) {
				outSet.add(conn);
			}
		}
		return outSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Connection> Set<T> getAllConnections(Class<T> instance) {
		Set<Connection> connections = getAllConnections();
		if (instance==null) {
			log.info("Instance is null, returning all connections");
			return null;
		}
		Set<T> outSet = new HashSet<>();
		for (Connection conn: connections) {
			if (instance.isAssignableFrom(conn.getClass())) {
				outSet.add((T)conn);
			}
		}
		return outSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Connection> Set<T> getAllConnections(NetworkLayer layer,
			Class<T> instance) {
		Set<Connection> connections = getAllConnections(layer);
		if (instance==null) {
			log.info("Layer is null, returning all connections");
			return null;
		}
		Set<T> outSet = new HashSet<>();
		for (Connection conn: connections) {
			if (conn !=null && instance.isAssignableFrom(conn.getClass())) {
				outSet.add((T)conn);
			}
		}
		return outSet;		
	}

	public void addConnectionPoint(ConnectionPoint cp) throws TopologyException {
		if (cp==null)
			throw new TopologyException("Connection point cannot be null");
		cpSet.add(cp);
	}

	public void removeConnectionPoint(ConnectionPoint cp)
			throws TopologyException {
		if (cp==null)
			throw new TopologyException("Connection point cannot be null");
		if (!cpSet.contains(cp))
			throw new TopologyException("Connection point not contained in network element");
		cpSet.remove(cp);
	}

	
	public String toString() {
		String out = "(ID: " + this.getID();
		out += ", CPs: " + this.getConnectionPoints(false) + ")";
		return out;
	}
	
}
