package io.github.mohitc.topology.algorithm;

import io.github.mohitc.topology.algorithm.constraint.PathConstraint;
import io.github.mohitc.topology.algorithm.filters.ConnectionFilter;
import io.github.mohitc.topology.algorithm.filters.impl.ConnectionDirectionalityFilter;
import io.github.mohitc.topology.algorithm.filters.impl.ConnectionLoopFilter;
import io.github.mohitc.topology.dto.PathDTO;
import io.github.mohitc.topology.primitives.Connection;
import io.github.mohitc.topology.primitives.ConnectionPoint;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class PathComputationAlgorithm {

  protected static final Logger log = LoggerFactory.getLogger(PathComputationAlgorithm.class);

  private final List<ConnectionFilter> connectionFilters = List.of(new ConnectionLoopFilter(),
      new ConnectionDirectionalityFilter());

  public List<PathSpan> getUsableConnections(PathSpan pathSpan, PathConstraint constraint) {
    List<PathSpan> usableConnections = new ArrayList<>();

    //only use connections with aEnd as currentHop
    for (Connection conn: pathSpan.getCurrentCp().getConnections()) {

      boolean doFilter = false;
      for (ConnectionFilter filter : connectionFilters) {
        if (filter.filter(pathSpan, conn, constraint)) {
          doFilter = true;
          break;
        }
      }
      if (!doFilter) {
        usableConnections.add(new PathSpan(pathSpan, conn));
      }
    }
    return usableConnections;
  }

  public PathDTO computePath(TopologyManager manager, ConnectionPoint aEnd, ConnectionPoint zEnd, PathConstraint constraint) throws TopologyException {
    PathSpan forwardPath = computeDirectedPath(manager, aEnd, zEnd, constraint);
    if (constraint.isDirected()) {
      //no need to compute reverse path
      return generatePath(manager, aEnd, zEnd, forwardPath, null, constraint);
    } else {
      if (constraint.isSymmetric()) {
        //flip sequence of connections and connection points
        List<ConnectionPoint> connPointSequence = new ArrayList<>(forwardPath.getOrderedConnectionPointSequence().size());
        connPointSequence.addAll(forwardPath.getOrderedConnectionPointSequence());
        Collections.reverse(connPointSequence);

        List<Connection> connSequence = new ArrayList<>(forwardPath.getOrderedConnectionSequence().size());
        connSequence.addAll(forwardPath.getOrderedConnectionSequence());
        Collections.reverse(connSequence);

        PathSpan backwardPath = new PathSpan(zEnd, connPointSequence, connSequence);
        return generatePath(manager, aEnd, zEnd, forwardPath, backwardPath, constraint);
      } else {
        //Compute backward path
        PathSpan backwardPath = computeDirectedPath(manager, zEnd, aEnd, constraint);
        return generatePath(manager, aEnd, zEnd, forwardPath, backwardPath, constraint);
      }
    }
  }

  protected PathDTO generatePath(TopologyManager manager, ConnectionPoint aEnd, ConnectionPoint zEnd, PathSpan forwardSequence, PathSpan reverseSequence, PathConstraint constraint) {
    PathDTO dto = new PathDTO();
    dto.setaEndId(aEnd.getID());
    dto.setzEndId(zEnd.getID());
    Optional.ofNullable(forwardSequence).ifPresent( v -> dto.setForwardConnectionSequence(v.getOrderedConnectionSequence().stream().map(Connection::getID).collect(Collectors.toList())));
    Optional.ofNullable(reverseSequence).ifPresent( v -> dto.setBackwardConnectionSequence(v.getOrderedConnectionSequence().stream().map(Connection::getID).collect(Collectors.toList())));
    Optional.ofNullable(forwardSequence).ifPresent( v -> dto.setForwardConnectionPointSequence(v.getOrderedConnectionPointSequence().stream().map(ConnectionPoint::getID).collect(Collectors.toList())));
    Optional.ofNullable(reverseSequence).ifPresent( v -> dto.setBackwardConnectionPointSequence(v.getOrderedConnectionPointSequence().stream().map(ConnectionPoint::getID).collect(Collectors.toList())));
    dto.setDirected(constraint.isDirected());
    dto.setStrict(true);
    return dto;
  }

  protected PathSpan computeDirectedPath(TopologyManager manager, ConnectionPoint aEnd, ConnectionPoint zEnd, PathConstraint constraint) throws TopologyException {
    //Check if input parameters are valid
    if (aEnd==null) {
      throw new TopologyException("aend cannot be null");
    }
    if (zEnd==null) {
      throw new TopologyException("zend cannot be null");
    }
    //Set of vertices that has been visited by paths already. Filter paths that revisit an already visited vertex
    Set<Integer> visitedVertices = new HashSet<>();
    visitedVertices.add(aEnd.getID());
    //the first hop connection can only be an outgoing link
    //Populate the sequence of potential paths using an outgoing link from the network ports
    //First connection should always be a link
    List<PathSpan> potentialPaths = getUsableConnections(new PathSpan(aEnd), constraint);

    //If connection list is not empty, there is at least one potential path to follow
    while(!potentialPaths.isEmpty()) {
      //Take the first potential path from the list and remove it from the list of potential paths
      PathSpan nextSpan = potentialPaths.removeFirst();
      //check if currentHop is zEnd, and if yes then return this connection
      if (zEnd.equals(nextSpan.getCurrentCp())) {
        return nextSpan;
      }
      //Trail cannot be terminated, extending trail using other connections
      List<PathSpan> allNewSpans = getUsableConnections(nextSpan, constraint);
      //get all connections from the current hop, and ignore the last connection in the list connPath (do not reuse connection)
      for (PathSpan span: allNewSpans) {
        if (visitedVertices.contains(span.getCurrentCp().getID())) {
          continue;
        }
        visitedVertices.add(span.getCurrentCp().getID());
        insertPathInList(potentialPaths, span);
      }
    }
    throw new TopologyException("Path not found in the test.topology");
  }


  //Sorting functions
  public int comparePath (PathSpan seq1, PathSpan seq2) {
    return seq1.getOrderedConnectionSequence().size() - seq2.getOrderedConnectionSequence().size();
  }

  public void insertPathInList(List<PathSpan> potentialPaths, PathSpan newPath) {
    for (int pos=potentialPaths.size()-1;pos>=0;pos--) {
      if (comparePath(newPath, potentialPaths.get(pos)) > 0) {
        potentialPaths.add(pos+1, newPath);
        return;
      }
    }
    //Path shorter than all other paths
    potentialPaths.addFirst(newPath);
  }

}
