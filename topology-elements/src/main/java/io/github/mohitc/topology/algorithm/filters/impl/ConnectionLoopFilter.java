package io.github.mohitc.topology.algorithm.filters.impl;

import io.github.mohitc.topology.algorithm.PathSpan;
import io.github.mohitc.topology.algorithm.constraint.PathConstraint;
import io.github.mohitc.topology.algorithm.filters.ConnectionFilter;
import io.github.mohitc.topology.primitives.Connection;

/** Filter to ensure that the connection does not form a loop
 *
 */
public class ConnectionLoopFilter implements ConnectionFilter {

  @Override
  public boolean filter(PathSpan pathSpan, Connection conn, PathConstraint constraint) {
    //null connections should be ignored
    //If the existing set of traversed connection points have both the endpoints in it, then the connection is forming a loop
    return conn == null || (pathSpan.getOrderedConnectionPointSequence().contains(conn.getaEnd())
        && pathSpan.getOrderedConnectionPointSequence().contains(conn.getzEnd()));
  }
}
