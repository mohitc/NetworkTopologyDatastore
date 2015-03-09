package com.topology.algorithm;

import com.topology.algorithm.constraint.PathConstraint;
import com.topology.primitives.*;
import com.topology.primitives.exception.TopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathComputationAlgorithm {

  protected static final Logger log = LoggerFactory.getLogger(PathComputationAlgorithm.class);

  public Set<Connection> getUsableConnections(ConnectionPoint cEnd, List<Connection> path, PathConstraint constraint) {
    Set<Connection> allConnections = cEnd.getConnections();
    Set<Connection> usableConnections = new HashSet<>();

    if (path == null)
      path = new ArrayList<>();

    //only use connections with aEnd as currentHop
    for (Connection conn: allConnections) {
      //Ignore trails
      if (Trail.class.isAssignableFrom(conn.getClass()))
        continue;
      //Existing path should not contain connection
      if (!path.contains(conn)) {
      //check if path is directed, and if so remove all links getting into the current point
        if (constraint.isDirected()) {
          if (conn.getaEnd().equals(cEnd)) {
            usableConnections.add(conn);
          }
        } else {
          //path is bidirectional
          if (constraint.isSymmetric()) {
            if (!conn.isDirected()) {
              usableConnections.add(conn);
            }
          } else {
            //path can be aSymmetric
            if (!conn.isDirected()) {
              usableConnections.add(conn);
            } else if (conn.getaEnd().equals(cEnd)) {
                usableConnections.add(conn);
              }
          }
        }
      }
    }
    return usableConnections;
  }

  public Path computePath(TopologyManager manager, ConnectionPoint aEnd, ConnectionPoint zEnd, PathConstraint constraint) throws TopologyException {
    List<Connection> forwardPath = computeDirectedPath(manager, aEnd, zEnd, constraint);
    if (constraint.isDirected()) {
      //no need to compute reverse path
      return generatePath(manager, aEnd, zEnd, forwardPath, null, constraint);
    } else {
      if (constraint.isSymmetric()) {
        //flip sequence of connections
        List<Connection> backwardPath = new ArrayList<>();
        for (Connection conn: forwardPath) {
          backwardPath.add(0, conn);
        }
        return generatePath(manager, aEnd, zEnd, forwardPath, backwardPath, constraint);
      } else {
        //Compute backward path
        List<Connection> backwardPath = computeDirectedPath(manager, zEnd, aEnd, constraint);
        return generatePath(manager, aEnd, zEnd, forwardPath, backwardPath, constraint);
      }
    }
  }

  protected Path generatePath(TopologyManager manager, ConnectionPoint aEnd, ConnectionPoint zEnd, List<Connection> forwardSequence, List<Connection> reverseSequence, PathConstraint constraint) {
    return manager.generatePathDef(aEnd, zEnd, forwardSequence, reverseSequence, constraint.isDirected(), true);
  }


  protected List<Connection> computeDirectedPath(TopologyManager manager, ConnectionPoint aEnd, ConnectionPoint zEnd, PathConstraint constraint) throws TopologyException {
    //Check if input parameters are valid
    if (aEnd==null)
      throw new TopologyException("aend cannot be null");
    if (zEnd==null)
      throw new TopologyException("zend cannot be null");
    //the first hop connection can only be an outgoing link
    List<List<Connection>> potentialPaths = new ArrayList<>();
    //Populate the sequence of potential paths using an outgoing link from the network ports
    //First connection should always be a link

    Set<Connection> usableAEndConnections = getUsableConnections(aEnd, null, constraint);
    for (Connection conn : usableAEndConnections) {
      List<Connection> connList = new ArrayList<>();
      connList.add(conn);
      potentialPaths.add(connList);
    }

    //If connection list is not empty, there is at least one potential path to follow
    while(potentialPaths.size()>0) {
      //Take the first potential path from the list and remove it from the list of potential paths
      List<Connection> connPath = potentialPaths.get(0);
      potentialPaths.remove(0);
      List<ConnectionPoint> orderedVertexSequence = getOrderedVertexSequence(connPath, aEnd);
      if (orderedVertexSequence!=null) {
        ConnectionPoint currentHop = orderedVertexSequence.get(orderedVertexSequence.size()-1);
        //check if currentHop is zEnd, and if yes then return this connection
        if (zEnd.equals(currentHop)) {
          return connPath;
        }
        //Trail cannot be terminated, extending trail using other connections
        Set<Connection> allConnections = getUsableConnections(currentHop, connPath, constraint);
        //get all connections from the current hop, and ignore the last connection in the list connPath (do not reuse connection)
        for (Connection connection: allConnections) {
          //Check for loops over ordered vertex sequence
          int count = 0;
          count = count + ((orderedVertexSequence.contains(connection.getaEnd()))? 1 : 0);
          count = count + ((orderedVertexSequence.contains(connection.getzEnd()))? 1 : 0);

          if (count==1) {
            //loop avoidance: only one end is contained in the ordered vertex sequence
            List<Connection> newPath = new ArrayList<>();
            newPath.addAll(connPath);
            newPath.add(connection);
            insertPathInList(potentialPaths, newPath);
          }
        }
      }
    }
    throw new TopologyException("Path not found in the test.topology");
  }


  //Sorting functions
  public int comparePath (List<Connection> seq1, List<Connection> seq2) {
    return seq1.size() - seq2.size();
  }

  public void insertPathInList(List<List<Connection>> potentialPaths, List<Connection> newPath) {
    for (int pos=potentialPaths.size()-1;pos>=0;pos--) {
      if (comparePath(newPath, potentialPaths.get(pos)) > 0) {
        potentialPaths.add(pos+1, newPath);
        return;
      }
    }
    //Path longer than all other paths
    potentialPaths.add(0, newPath);
  }

  private List<ConnectionPoint> getOrderedVertexSequence (List<Connection> connections, ConnectionPoint aEnd) {
    List<ConnectionPoint> orderedList = new ArrayList<>();
    if ((connections==null) || (aEnd==null))
      return null;
    orderedList.add(aEnd);
    ConnectionPoint currentHop = aEnd;
    ConnectionPoint nextHop;
    for (Connection connection: connections) {
      if (connection.getaEnd().getID()==currentHop.getID()) {
        //aEnd connection point is the current hop, next hop is zend
        nextHop = connection.getzEnd();
      } else if (connection.getzEnd().getID()==currentHop.getID()) {
        nextHop = connection.getaEnd();
      } else {
        //Neither aEnd nor zEnd are the current connection point, this is an error
        return null;
      }
      orderedList.add(nextHop);
      currentHop = nextHop;
    }
    return orderedList;
  }

}
