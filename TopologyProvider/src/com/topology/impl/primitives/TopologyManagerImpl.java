package com.topology.impl.primitives;

import java.util.*;

import com.helpers.benchmark.annotation.Benchmark;
import com.topology.primitives.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topology.primitives.exception.TopologyException;

public class TopologyManagerImpl implements TopologyManager {

  private String identifier;

	private Set<Integer> idSet;

	private Map<Integer, TopologyElement> topoElements;

	private Map<Integer, NetworkElement> networkElements;

	private Map<Integer, ConnectionPoint> connectionPoints;

	private Map<Integer, Connection> connections;

	private Map<NEPair, Set<Connection>> neConnections;

	private static final Logger log = LoggerFactory.getLogger(TopologyManagerImpl.class);

	private Random generator;

	//Class to store key for connections between network elements
	private class NEPair {
		private int aEndID;

		private int zEndID;

		NEPair(int aEnd, int zEnd) {
			this.aEndID = aEnd;
			this.zEndID = zEnd;
		}

		public int hashCode() {
			int out = aEndID + zEndID;
			String hashString = out + ""; 
			return hashString.hashCode();
		}

		public boolean equals(Object o) {
			if (o!=null) {
				if (o instanceof NEPair) {
					NEPair other = (NEPair)o;
					if (((other.aEndID == aEndID) && (other.zEndID == zEndID)) || ((other.aEndID == zEndID) && (other.zEndID == aEndID))) {
						return true;
					}
				}
			}
			return false;
		}
	}

	public TopologyManagerImpl(String identifier) {
		idSet = new HashSet<>();
		//Initialize HashMaps
		topoElements = new HashMap<>();
		networkElements = new HashMap<>();
		connectionPoints = new HashMap<>();
		connections = new HashMap<>();
		neConnections = new HashMap<>();
		generator = new Random();
    this.identifier = identifier;
	}

  @Override
  public String getIdentifier() {
    return identifier;
  }

  @Override
	public boolean hasElement(int id) {
		return topoElements.containsKey(id);
	}

	@Override
	public <T extends TopologyElement> boolean hasElement(int id,
			Class<T> instance) {
		if (hasElement(id)) {
			//Element exists
			try {
				TopologyElement element = getElementByID(id);
				if (instance.isAssignableFrom(element.getClass())){
					return true;
				}
			} catch (TopologyException e){
				//should not happer
				log.error("Unexpected exception thrown en extracting network element: ", e);
			}
		} 
		return false;
	}
	
	
	private NetworkElement getNetworkElementFromCp(ConnectionPoint cp) {
		while((cp!=null) && (cp.getParent()!=null)) {
			if (NetworkElement.class.isAssignableFrom(cp.getParent().getClass())) {
				return (NetworkElement)cp.getParent();
			} else if (ConnectionPoint.class.isAssignableFrom(cp.getParent().getClass())) {
				cp = (ConnectionPoint)cp.getParent();
			} else {
				log.error("Parent of connection point is not a connection point or a Network Element");
				return null;
			}
		}
		return null;
	}

	private <T extends Connection> void addToNeConnections(NEPair key, T value) {
		if ((key!=null) && (value!=null)) {
			if (neConnections.containsKey(key)) {
				neConnections.get(key).add(value);
			} else {
				Set<Connection> values = new HashSet<>();
				values.add(value);
				neConnections.put(key, values);
			}
		}
	}

	@Override
	public <T extends TopologyElement> Set<T> getAllElements(Class<T> instance) {
        Set<T> elementSet = new HashSet<>();
        Collection<TopologyElement> values = topoElements.values();
        for (TopologyElement element: values) {
            if (element!=null)
                if (instance.isAssignableFrom(element.getClass())) {
                    elementSet.add((T)element);
                }
        }
		return elementSet;
	}

	private int generateRandomID() {
		do {
			int id = generator.nextInt();
			if (!idSet.contains(id)) {
				return id;
			}
		} while(true);
	}


	@Override
  @Benchmark
	public NetworkElement createNetworkElement() throws TopologyException {
		synchronized(this) {
			NetworkElement ne = new NetworkElementImpl(this, generateRandomID());
			idSet.add (ne.getID());
			topoElements.put(ne.getID(), ne);
			networkElements.put(ne.getID(), ne);
			log.info("New Network Element Created: " + ne);
			return ne;
		}
	}


	@Override
	public ConnectionPoint createConnectionPoint(TopologyElement parent)
			throws TopologyException {
        ConnectionPoint cp = new ConnectionPointImpl(this, generateRandomID(), parent);
        populateConnectionPointAssociations(cp, parent);
        log.info("New Connection Point Created: " + cp);
        return cp;
	}


	@Override
	public Port createPort(TopologyElement parent) throws TopologyException {
		Port cp = new PortImpl(this, generateRandomID(), parent);
        populateConnectionPointAssociations(cp, parent);
		log.info("New Port Created: " + cp);
        return cp;
	}

    private void populateConnectionPointAssociations(ConnectionPoint cp, TopologyElement parent) throws TopologyException {
        if (parent==null) {
            //Empty ports are allowed, but cannot be assigned to parent afterwards
            synchronized(this) {
                idSet.add (cp.getID());
                connectionPoints.put(cp.getID(), cp);
                topoElements.put(cp.getID(), cp);
                return;
            }

        }
        //parent is not null
        TopologyElement elemParent = this.getElementByID(parent.getID());
        //parent found, check if instance of Port or Network Element
        if (!(NetworkElement.class.isAssignableFrom(elemParent.getClass()) || (Port.class.isAssignableFrom(elemParent.getClass())))) {
            throw new TopologyException("Parent must be of type Network element or Port");
        }
        synchronized(this) {
            idSet.add (cp.getID());
            connectionPoints.put(cp.getID(), cp);
            topoElements.put(cp.getID(), cp);
            if (NetworkElementImpl.class.isAssignableFrom(elemParent.getClass())){
                ((NetworkElementImpl)elemParent).addConnectionPoint(cp);
            } else if (PortImpl.class.isAssignableFrom(elemParent.getClass())){
                ((PortImpl)elemParent).addContainedConnectionPoint(cp);
            }
        }
    }


	@Override
	public Link createLink(int startCpID, int endCpID) throws TopologyException {
		//Check if start and end connection points are in the test.topology manager
		ConnectionPoint localStartCP = this.getElementByID(startCpID, ConnectionPoint.class);
		ConnectionPoint localEndCP = this.getElementByID(endCpID, ConnectionPoint.class);
		//Endpoints of a link should have different endpoints
		NetworkElement startNe = getNetworkElementFromCp(localStartCP);
		NetworkElement endNe = getNetworkElementFromCp(localEndCP);
		if ((startNe!=null) && (endNe!=null) && (startNe == endNe)) {
			throw new TopologyException ("Endpoints of a link must belong to different network elements");
		}
		synchronized (this) {
			//create new link
			Link link = new LinkImpl(this, generateRandomID(), localStartCP, localEndCP);
			idSet.add(link.getID());
			connections.put(link.getID(), link);
			topoElements.put(link.getID(), link);
			//add connection to connection point
			((ConnectionPointImpl)localStartCP).addConnection(link);
			((ConnectionPointImpl)localEndCP).addConnection(link);
			//Add to neConnectionMap
			startNe = getNetworkElementFromCp(localStartCP);
			endNe = getNetworkElementFromCp(localEndCP);
			if ((startNe!=null)&&(endNe!=null)) {
				NEPair pair = new NEPair(startNe.getID(), endNe.getID());				
				addToNeConnections(pair, link);
			}
			return link;
		}
	}

	@Override
	public CrossConnect createCrossConnect(int startCpID, int endCpID) throws TopologyException {
		//Check if start and end connection points are in the test.topology manager
		ConnectionPoint localStartCP = this.getElementByID(startCpID, ConnectionPoint.class);
		ConnectionPoint localEndCP = this.getElementByID(endCpID, ConnectionPoint.class);
		NetworkElement startNe = getNetworkElementFromCp(localStartCP);
		NetworkElement endNe = getNetworkElementFromCp(localEndCP);
		if (!((startNe!=null)&&(endNe!=null))) {
			throw new TopologyException("Both connection points for a cross connect must be contained in a network element");
		}
		if (!startNe.equals(endNe)) {
			throw new TopologyException("Both connection points for a cross connect must be contained in a network element");
		}

		synchronized (this) {
			//create new link
			CrossConnect cc = new CrossConnectImpl(this, generateRandomID(), localStartCP, localEndCP);
			idSet.add(cc.getID());
			connections.put(cc.getID(), cc);
			topoElements.put(cc.getID(), cc);
			//add connection to connection point
			((ConnectionPointImpl)localStartCP).addConnection(cc);
			((ConnectionPointImpl)localEndCP).addConnection(cc);
			//Add to neConnectionMap
			NEPair pair = new NEPair(startNe.getID(), endNe.getID());				
			addToNeConnections(pair, cc);
			return cc;
		}
	}

	@Override
	public Trail createTrail(int startCpID, int endCpID) throws TopologyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeTopologyElement(int id) throws TopologyException {
    TopologyElement te = getElementByID(id);
    if (te!=null) {
      if (NetworkElement.class.isAssignableFrom(te.getClass())) {
        removeNetworkElement(id);
      } else if (ConnectionPoint.class.isAssignableFrom(te.getClass())) {
        removeConnectionPoint(id);
      } else if (Connection.class.isAssignableFrom(te.getClass())) {
        removeConnection(id);
      }
    } else {
      log.error("Element not found in TopoElements map");
      throw new TopologyException("Element not found in TopoElements map");
    }
	}


	@Override
	public void removeNetworkElement(int id) throws TopologyException {
		NetworkElement ne = this.getElementByID(id, NetworkElement.class);
		//check if there are no connection points in the network element
		Set<ConnectionPoint> neCps=ne.getConnectionPoints(false);
		if ((neCps!=null) && (neCps.size()>0)) {
			throw new TopologyException("Network element cannot be deleted while it contains connection points");
		}
		//Network connection points are null
		//removing from maps
		topoElements.remove(ne.getID());
		idSet.remove(ne.getID());
		networkElements.remove(ne.getID());
		log.info("Network Element removed successfully");
	}


	@Override
	public void removeConnectionPoint(int id) throws TopologyException {
		ConnectionPoint cp = this.getElementByID(id, ConnectionPoint.class);
		//Connection point exists
		if (Port.class.isAssignableFrom(cp.getClass())) {
			//check for contained connection points
			Port port = (Port) cp;
			if (!((port.getContainedConnectionPoints()==null)||(port.getContainedConnectionPoints().size()==0))) {
				throw new TopologyException("Port contains connection points that must be removed before removing port");
			}
		}
		//Connection point is not a port
		//Check if there are any existing connections from the connection point
		if ((cp.getConnections()==null) || (cp.getConnections().size()==0)) {
			//No connections on connection point
			//check if parent exists and remove connection point from parent
			if (cp.getParent()!=null) {
				if (NetworkElementImpl.class.isAssignableFrom(cp.getParent().getClass())) {
					((NetworkElementImpl)cp.getParent()).removeConnectionPoint(cp);
				} else if (PortImpl.class.isAssignableFrom(cp.getParent().getClass())) {
					((PortImpl)cp.getParent()).removeContainedConnectionPoint(cp);
				}
				//remove from maps
				topoElements.remove(cp.getID());
				idSet.remove(cp.getID());
				connectionPoints.remove(cp.getID());
				log.info("Connection Point removed successfully");
			}
		} else {
			throw new TopologyException("Connection point cannot be removed untill all connections have been removed from them");
		}


	}


	@Override
	public void removePort(int id) throws TopologyException {
		//logic for removing ports included in removing connection points
		removeConnectionPoint(id);
	}


	@Override
	public void removeConnection(int id) throws TopologyException {
    Connection te = getElementByID(id, Connection.class);
    if (te!=null) {
      if (Link.class.isAssignableFrom(te.getClass())) {
        removeLink(id);
      } else if (Trail.class.isAssignableFrom(te.getClass())) {
        removeTrail(id);
      } else if (CrossConnect.class.isAssignableFrom(te.getClass())) {
        removeCrossConnect(id);
      }
    } else {
      log.error("Element not found in TopoElements map");
      throw new TopologyException("Element not found in TopoElements map");
    }
	}


	private void removeConnectionAssociations(Connection conn) throws TopologyException {
		topoElements.remove(conn.getID());
		idSet.remove(conn.getID());
		connections.remove(conn.getID());
		//remove link from the endpoints
		((ConnectionPointImpl)conn.getaEnd()).removeConnection(conn);
		((ConnectionPointImpl)conn.getzEnd()).removeConnection(conn);
		//Remove from NEConnectionMap
		NetworkElement startNe = getNetworkElementFromCp(conn.getaEnd());
		NetworkElement endNe = getNetworkElementFromCp(conn.getzEnd());
		if ((startNe!=null) && (endNe!=null)) {
			NEPair pair = new NEPair(startNe.getID(), endNe.getID());
			if (neConnections.containsKey(pair)) {
				Set<Connection> connections = neConnections.get(pair);
				if (connections.contains(conn)) {
					connections.remove(conn);
					if (connections.size()==0) {
						neConnections.remove(pair);
					}
				}
			}
		}


	}

	@Override
	public void removeLink(int id) throws TopologyException {
		Link link = this.getElementByID(id, Link.class);
		//Check if link has any reservations
		//TODO implement this
		//remove link from maps 
		removeConnectionAssociations(link);
		log.info("Link removed successfully");
	}


	@Override
	public void removeCrossConnect(int id) throws TopologyException {
		Link link = this.getElementByID(id, Link.class);
		//Check if link has any reservations
		//TODO implement this
		//remove link from maps 
		removeConnectionAssociations(link);
		log.info("Cross Connect removed successfully");
	}

  @Override
  public void removeTrail(int id) throws TopologyException {
    //TODO implement this
  }


  @Override
	public TopologyElement getElementByID(int id) throws TopologyException {
		if (!topoElements.containsKey(id)) {
			throw new TopologyException("Element with ID: " + id + " does not exist");
		}
		return topoElements.get(id);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T extends TopologyElement> T getElementByID(int id,
			Class<T> instance) throws TopologyException {
		TopologyElement element = getElementByID(id);
		if (instance.isAssignableFrom(element.getClass()))
			return (T)element;
		else 
			throw new TopologyException("Element with ID: " + id + " cannot be assigned to class " + instance);
	}

  @Override
  public <T extends TopologyElement> Set<T> getElementsByLabel(String label, Class<T> instance) {
    Set<T> outSet = new HashSet<>();
    if (label==null) {
      return outSet;
    }
    Collection<TopologyElement> elements = topoElements.values();
    for (TopologyElement element : elements) {
      if (element!=null) {
        if (instance.isAssignableFrom(element.getClass())) {
          if ((element.getLabel()!=null) && (label.equalsIgnoreCase(element.getLabel()))) {
            outSet.add((T)element);
          }
        }
      }
    }
    return outSet;
  }

  @Override
  public <T extends TopologyElement> T getSingleElementByLabel(String label, Class<T> instance) throws TopologyException {
    if (label==null) {
      throw new TopologyException("Label cannot be null");
    }
    Collection<TopologyElement> elements = topoElements.values();
    for (TopologyElement element : elements) {
      if (element!=null) {
        if (instance.isAssignableFrom(element.getClass())) {
          if ((element.getLabel()!=null) && (label.equalsIgnoreCase(element.getLabel()))) {
            return (T)element;
          }
        }
      }
    }
    throw new TopologyException("No element with label: "  +label + " and class : " + instance + " found in the test.topology");
  }

  @Override
    public Set<Connection> getConnections(int startNeID, int endNeID) throws TopologyException {
        log.info("Checking if network elements indicated by the id exist");
        getElementByID(startNeID, NetworkElement.class);
        getElementByID(endNeID, NetworkElement.class);

        NEPair pair = new NEPair(startNeID, endNeID);
        if (neConnections.containsKey(pair)) {
            return neConnections.get(pair);
        } else
            return new HashSet<>();
    }

    @Override
    public <T extends Connection> Set<T> getConnections(int startNeID, int endNeID, Class<T> instance) throws TopologyException {
        if (instance==null)
            throw new TopologyException("Instance cannot be null");
        Set<Connection> connections = getConnections(startNeID, endNeID);
        Set<T> filteredConnections = new HashSet<>();
        if (connections!=null) {
            for (Connection conn: connections) {
                if (conn!=null) {
                    if (instance.isAssignableFrom(conn.getClass())) {
                        filteredConnections.add((T) conn);
                    }
                }
            }
        }
        return filteredConnections;
    }

    @Override
    public Set<Connection> getConnections(int startNeID, int endNeID, NetworkLayer layer) throws TopologyException {
        if (layer==null)
            throw new TopologyException("Layer cannot be null");
        Set<Connection> connections = getConnections(startNeID, endNeID);
        Set<Connection> filteredConnections = new HashSet<>();
        if (connections!=null) {
            for (Connection conn: connections) {
                if ((conn!=null)  && (conn.getLayer() != null)) {
                    if (layer.equals(conn.getLayer())) {
                        filteredConnections.add(conn);
                    }
                }
            }
        }
        return filteredConnections;
    }

    @Override
    public <T extends Connection> Set<T> getConnections(int startNeID, int endNeID, NetworkLayer layer, Class<T> instance) throws TopologyException {
        if (instance==null)
            throw new TopologyException("Instance cannot be null");
        Set<Connection> connections = getConnections(startNeID, endNeID, layer);
        Set<T> filteredConnections = new HashSet<>();
        if (connections!=null) {
            for (Connection conn: connections) {
                if (conn!=null) {
                    if (instance.isAssignableFrom(conn.getClass())) {
                        filteredConnections.add((T) conn);
                    }
                }
            }
        }
        return filteredConnections;    }

    @Override
    public Set<Connection> getConnections(int startNeID, int endNeID, boolean isDirected) throws TopologyException {
		log.info("Checking if network elements indicated by the id exist");
		getElementByID(startNeID, NetworkElement.class);
		getElementByID(endNeID, NetworkElement.class);

        Set<Connection> filteredConnections = new HashSet<>();
		NEPair pair = new NEPair(startNeID, endNeID);
		if (neConnections.containsKey(pair)) {
			Set<Connection> connections = neConnections.get(pair);
			//filter connections from start NE to end NE
			for (Connection conn: connections) {
                if (!isDirected) {
                    //looking for bidirectional connections
                    if (!conn.isDirected()) {
                        filteredConnections.add(conn);
                    }
                } else {
                    if (conn.isDirected()) {
                        //looking from directed connections form the start network element
    					NetworkElement startNe = getNetworkElementFromCp(conn.getaEnd());
	    				if ((startNe!=null) && (startNe.getID()==startNeID)) {
                            filteredConnections.add(conn);
                        }
					}
				}
			}
		}
        return filteredConnections;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Connection> Set<T> getConnections(int startNeID,
			int endNeID, boolean isDirected, Class<T> instance) throws TopologyException {
		if (instance==null)
			throw new TopologyException("Instance cannot be null");
		Set<Connection> connections = getConnections(startNeID, endNeID, isDirected);
		Set<T> filteredConnections = new HashSet<>();
		if (connections!=null) {
			for (Connection conn: connections) {
				if (conn!=null) {
					if (instance.isAssignableFrom(conn.getClass())) {
						filteredConnections.add((T) conn);
					}
				}
			}
		}
		return filteredConnections;
	}

	@Override
	public Set<Connection> getConnections(int startNeID,
			int endNeID, boolean isDirected, NetworkLayer layer) throws TopologyException {
		if (layer==null)
			throw new TopologyException("Layer cannot be null");
		Set<Connection> connections = getConnections(startNeID, endNeID, isDirected);
		Set<Connection> filteredConnections = new HashSet<>();
		if (connections!=null) {
			for (Connection conn: connections) {
                if ((conn!=null)  && (conn.getLayer() != null)) {
					if (conn.getLayer().equals(layer)) {
						filteredConnections.add(conn);
					}
				}
			}
		}
		return filteredConnections;
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T extends Connection> Set<T> getConnections(int startNeID,
			int endNeID, boolean isDirected, NetworkLayer layer,
			Class<T> instance) throws TopologyException {
		if (instance==null)
			throw new TopologyException("Instance cannot be null");
		Set<Connection> connections = getConnections(startNeID, endNeID, isDirected, layer);		
		Set<T> filteredConnections = new HashSet<>();
		if (connections!=null) {
			for (Connection conn: connections) {
				if (conn!=null) {
					if (instance.isAssignableFrom(conn.getClass())) {
						filteredConnections.add((T) conn);
					}
				}
			}
		}
		return filteredConnections;
	}

  @Override
  public Path generatePathDef(ConnectionPoint aEnd, ConnectionPoint zEnd, List<Connection> forwardSequence, List<Connection> backwardSequence, boolean directed, boolean strict) {
    Path path = new PathImpl();
    //TODO include validation methods for path components
    path.setaEnd(aEnd);
    path.setzEnd(zEnd);
    path.setStrict(strict);
    path.setDirected(directed);
    path.setForwardConnectionSequence(forwardSequence);
    path.setBackwardConnectionSequence(backwardSequence);
    return path;
  }


}
