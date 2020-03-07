package com.topology.algorithm.filters.impl;

import com.topology.algorithm.constraint.PathConstraint;
import com.topology.algorithm.filters.ConnectionFilter;
import com.topology.primitives.Connection;
import com.topology.primitives.ConnectionPoint;

/** The Connection Directionality filter checks if a connection is uni/bidirectional, and filters out connection accordingly
 *
 */
public class ConnectionDirectionalityFilter implements ConnectionFilter {

  private PathConstraint constraint;

  private ConnectionPoint cEnd;

  public ConnectionDirectionalityFilter(PathConstraint constraint, ConnectionPoint cEnd) {
    this.constraint = constraint;
    this.cEnd = cEnd;
  }


  @Override
  public boolean filter(Connection conn) {
    if (constraint.isDirected()) {
      return !conn.getaEnd().equals(cEnd);
    } else {
      //path is bidirectional
      if (constraint.isSymmetric()) {
        return conn.isDirected();
      } else {
        //path can be aSymmetric
        if (!conn.isDirected()) {
          return false;
        } else return !conn.getaEnd().equals(cEnd);
      }
    }
    //if conditions are not met, filter connection
  }
}
