package com.topology.algorithm.filters.impl;

import com.topology.algorithm.PathSpan;
import com.topology.algorithm.constraint.PathConstraint;
import com.topology.algorithm.filters.ConnectionFilter;
import com.topology.primitives.Connection;

/** Filter to ensure that the connection does not form a loop
 *
 */
public class ConnectionLoopFilter implements ConnectionFilter {

  @Override
  public boolean filter(PathSpan pathSpan, Connection conn, PathConstraint constraint) {
    //null connections should be ignored
    if (conn == null)
      return true;
    //If the existing set of traversed connection points have both the endpoints in it, then the connection is forming a loop
    return (pathSpan.getOrderedConnectionPointSequence().contains(conn.getaEnd()))
        && (pathSpan.getOrderedConnectionPointSequence().contains(conn.getzEnd()));
  }
}
