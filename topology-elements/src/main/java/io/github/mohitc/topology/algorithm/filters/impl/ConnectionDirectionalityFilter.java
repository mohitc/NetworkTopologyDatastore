package io.github.mohitc.topology.algorithm.filters.impl;

import io.github.mohitc.topology.algorithm.PathSpan;
import io.github.mohitc.topology.algorithm.constraint.PathConstraint;
import io.github.mohitc.topology.algorithm.filters.ConnectionFilter;
import io.github.mohitc.topology.primitives.Connection;

/** The Connection Directionality filter checks if a connection is uni/bidirectional, and filters out connection accordingly
 *
 */
public class ConnectionDirectionalityFilter implements ConnectionFilter {

  @Override
  public boolean filter(PathSpan span, Connection conn, PathConstraint constraint) {
    if (constraint.isDirected()) {
      return !conn.getaEnd().equals(span.getCurrentCp());
    } else {
      //path is bidirectional
      if (constraint.isSymmetric()) {
        return conn.isDirected();
      } else {
        //path can be aSymmetric
        return conn.isDirected() && !conn.getaEnd().equals(span.getCurrentCp());
      }
    }
    //if conditions are not met, filter connection
  }
}
