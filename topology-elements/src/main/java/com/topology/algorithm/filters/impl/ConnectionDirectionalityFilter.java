package com.topology.algorithm.filters.impl;

import com.topology.algorithm.PathSpan;
import com.topology.algorithm.constraint.PathConstraint;
import com.topology.algorithm.filters.ConnectionFilter;
import com.topology.primitives.Connection;

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
        if (!conn.isDirected()) {
          return false;
        } else return !conn.getaEnd().equals(span.getCurrentCp());
      }
    }
    //if conditions are not met, filter connection
  }
}
