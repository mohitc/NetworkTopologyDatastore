package io.github.mohitc.topology.algorithm;

import io.github.mohitc.topology.algorithm.constraint.PathConstraint;
import io.github.mohitc.topology.dto.PathDTO;
import io.github.mohitc.topology.primitives.Connection;
import io.github.mohitc.topology.primitives.ConnectionPoint;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.exception.TopologyException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MultipathComputationAlgorithm extends PathComputationAlgorithm {
  public List<PathDTO> computePaths(TopologyManager manager, ConnectionPoint aEnd, ConnectionPoint zEnd, PathConstraint constraint) throws TopologyException {
    if (constraint.getPathCount()==1) {
      return List.of(computePath(manager, aEnd, zEnd, constraint));
    }
    List<PathSpan> forwardPaths = computeDirectedPaths(manager, aEnd, zEnd, constraint);
    if (constraint.isDirected()) {
      //no need to compute reverse path
      return forwardPaths.stream().map(forwardPath -> generatePath(manager, aEnd, zEnd, forwardPath, null, constraint)).collect(Collectors.toList());
    } else {
      return forwardPaths.stream().map(forwardPath -> {
        //flip sequence of connections and connection points
        List<ConnectionPoint> connPointSequence = new ArrayList<>(forwardPath.getOrderedConnectionPointSequence().size());
        connPointSequence.addAll(forwardPath.getOrderedConnectionPointSequence());
        Collections.reverse(connPointSequence);

        List<Connection> connSequence = new ArrayList<>(forwardPath.getOrderedConnectionSequence().size());
        connSequence.addAll(forwardPath.getOrderedConnectionSequence());
        Collections.reverse(connSequence);

        PathSpan backwardPath = new PathSpan(zEnd, connPointSequence, connSequence);
        return generatePath(manager, aEnd, zEnd, forwardPath, backwardPath, constraint);
      }).collect(Collectors.toList());
    }
  }

  protected List<PathSpan> computeDirectedPaths(TopologyManager manager, ConnectionPoint aEnd, ConnectionPoint zEnd, PathConstraint constraint) throws TopologyException {
    //Check if input parameters are valid
    if (aEnd==null) {
      throw new TopologyException("aEnd cannot be null");
    }
    if (zEnd==null) {
      throw new TopologyException("zEnd cannot be null");
    }
    //the first hop connection can only be an outgoing link
    //Populate the sequence of potential paths using an outgoing link from the network ports
    //First connection should always be a link
    List<PathSpan> potentialPaths = getUsableConnections(new PathSpan(aEnd), constraint);
    List<PathSpan> computedPaths = new ArrayList<>();

    //If connection list is not empty, there is at least one potential path to follow
    while(!potentialPaths.isEmpty()) {
      //Take the first potential path from the list and remove it from the list of potential paths
      PathSpan nextSpan = potentialPaths.removeFirst();
      //check if currentHop is zEnd, and if yes then return this connection
      if (zEnd.equals(nextSpan.getCurrentCp())) {
        computedPaths.add(nextSpan);
        if (computedPaths.size()>=constraint.getPathCount()) {
          break;
        }
        continue;
      }
      //Trail cannot be terminated, extending trail using other connections
      List<PathSpan> allNewSpans = getUsableConnections(nextSpan, constraint);
      //get all connections from the current hop, and ignore the last connection in the list connPath (do not reuse connection)
      for (PathSpan span: allNewSpans) {
        insertPathInList(potentialPaths, span);
      }
    }
    if (!computedPaths.isEmpty()) {
      return computedPaths;
    }
    throw new TopologyException("Path not found in the test.topology");
  }

}
