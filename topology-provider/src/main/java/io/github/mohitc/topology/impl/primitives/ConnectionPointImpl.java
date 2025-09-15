package io.github.mohitc.topology.impl.primitives;

import io.github.mohitc.topology.primitives.*;
import io.github.mohitc.topology.primitives.exception.TopologyException;

import java.util.HashSet;
import java.util.Set;

public class ConnectionPointImpl extends TopologyElementImpl implements ConnectionPoint {

	private TopologyElement parent;

	private final Set<Connection> connections;

	public ConnectionPointImpl(TopologyManager manager, int id) {
		super(manager, id);
		this.connections = new HashSet<>();
	}

	public ConnectionPointImpl(TopologyManager manager, int id, TopologyElement parent) throws TopologyException {
		super(manager, id);
		this.connections = new HashSet<>();
		this.parent = parent;
	}

	@Override
	public Set<Connection> getConnections() {
		return connections;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Connection> Set<T> getConnections(Class<T> instance) {
		Set<T> out = new HashSet<>();
		for (Connection conn: connections) {
			if (instance.isAssignableFrom(conn.getClass())) {
				out.add((T)conn);
			}
		}
		return out;
	}

	@Override
	public Set<Connection> getConnections(NetworkLayer layer) {
		Set<Connection> outSet = new HashSet<>();
    if (layer==null) {
      return outSet;
    }
		for (Connection conn: connections) {
			if (conn.getLayer().equals(layer)) {
				outSet.add(conn);
			}
		}
		return outSet;
	}

	@Override
	public <T extends Connection> Set<T> getConnections(NetworkLayer layer,
			Class<T> instance) {
		Set<T> inSet = getConnections(instance);
		Set<T> outSet = new HashSet<>();
		for (T conn: inSet) {
			if (conn.getLayer().equals(layer)) {
				outSet.add(conn);
			}
		}
		return outSet;
	}

	@Override
	public TopologyElement getParent() {
		return parent;
	}

	public void addConnection(Connection connection) throws TopologyException {
		if (connection==null) {
      throw new TopologyException("Connection cannot be null");
    }
		//at least one end (aEnd / zEnd should be terminating at the connection
		if (!(connection.getaEnd().equals(this) || connection.getzEnd().equals(this))) {
			throw new TopologyException("Connection should have at least one end point terminating at the specified connection point");
		}
		connections.add(connection);
	}

	public void removeConnection(Connection connection)
			throws TopologyException {
		if (connection==null) {
      throw new TopologyException("Connection cannot be null");
    }
    connections.remove(connection);
	}

}
