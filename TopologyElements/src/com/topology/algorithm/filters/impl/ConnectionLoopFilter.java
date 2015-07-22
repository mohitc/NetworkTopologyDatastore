package com.topology.algorithm.filters.impl;

import com.topology.algorithm.filters.ConnectionFilter;
import com.topology.primitives.Connection;
import com.topology.primitives.ConnectionPoint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Filter to ensure that the connection does not form a loop
 *
 */
public class ConnectionLoopFilter implements ConnectionFilter {

  private Set<ConnectionPoint> traversedCPs;

  public ConnectionLoopFilter (Set<ConnectionPoint> traversedCPs) {
    if (traversedCPs!=null) {
      this.traversedCPs = traversedCPs;
    } else
      this.traversedCPs = new HashSet<>();

  }

  public ConnectionLoopFilter(List<Connection> usedConnections) {
    traversedCPs = new HashSet<>();
    if (usedConnections!=null) {
      for (Connection conn: usedConnections) {
        traversedCPs.add(conn.getaEnd());
        traversedCPs.add(conn.getzEnd());
      }
    }
  }

  @Override
  public boolean filter(Connection conn) {
    //null connections should be ignored
    if (conn == null)
      return true;
    //If the existing set of traversed connection points have both the endpoints in it, then the connection is forming a loop
    return (traversedCPs.contains(conn.getaEnd())) && (traversedCPs.contains(conn.getzEnd()));
  }
}
