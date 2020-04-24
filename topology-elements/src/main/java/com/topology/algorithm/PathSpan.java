package com.topology.algorithm;

import com.topology.primitives.Connection;
import com.topology.primitives.ConnectionPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PathSpan {

  private final ConnectionPoint aEnd;

  private final List<ConnectionPoint> orderedConnectionPointSequence;

  private final List<Connection> orderedConnectionSequence;

  public PathSpan(ConnectionPoint aEnd) {
    this.aEnd = aEnd;
    this.orderedConnectionPointSequence = Collections.singletonList(aEnd);
    this.orderedConnectionSequence = Collections.emptyList();
  }

  public PathSpan(PathSpan oldSpan, Connection nextConn) {
    this.aEnd = oldSpan.aEnd;
    ConnectionPoint nextCp = oldSpan.getCurrentCp().equals(nextConn.getaEnd()) ? nextConn.getzEnd() : nextConn.getaEnd();
    this.orderedConnectionPointSequence = new ArrayList<>(oldSpan.orderedConnectionPointSequence.size() + 1);
    orderedConnectionPointSequence.addAll(oldSpan.orderedConnectionPointSequence);
    orderedConnectionPointSequence.add(nextCp);
    this.orderedConnectionSequence = new ArrayList<>(oldSpan.orderedConnectionSequence.size() + 1);
    orderedConnectionSequence.addAll(oldSpan.orderedConnectionSequence);
    orderedConnectionSequence.add(nextConn);
  }

  public PathSpan(ConnectionPoint aEnd, List<ConnectionPoint> orderedConnectionPointSequence, List<Connection> orderedConnectionSequence) {
    this.aEnd = aEnd;
    this.orderedConnectionSequence = orderedConnectionSequence;
    this.orderedConnectionPointSequence = orderedConnectionPointSequence;
  }

  public ConnectionPoint getaEnd() {
    return aEnd;
  }

  public List<ConnectionPoint> getOrderedConnectionPointSequence() {
    return orderedConnectionPointSequence;
  }

  public List<Connection> getOrderedConnectionSequence() {
    return orderedConnectionSequence;
  }

  public ConnectionPoint getCurrentCp() {
    return this.orderedConnectionPointSequence.get(orderedConnectionPointSequence.size()-1);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PathSpan pathSpan = (PathSpan) o;
    return Objects.equals(aEnd, pathSpan.aEnd) &&
        Objects.equals(orderedConnectionPointSequence, pathSpan.orderedConnectionPointSequence) &&
        Objects.equals(orderedConnectionSequence, pathSpan.orderedConnectionSequence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aEnd, orderedConnectionPointSequence, orderedConnectionSequence);
  }
}
