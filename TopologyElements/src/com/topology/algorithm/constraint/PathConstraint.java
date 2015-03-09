package com.topology.algorithm.constraint;

public class PathConstraint {

  //Boolean to indicate that path is directed
  private boolean directed;

  //Boolean to indicate that path is symmetric
  private boolean symmetric;

  //integer to indicate number of paths
  private int pathCount = 1;

  public PathConstraint(boolean directed, boolean symmetric) {
    this.directed = directed;
    this.symmetric = symmetric;
  }

  public boolean isDirected() {
    return directed;
  }

  public boolean isSymmetric() {
    return symmetric;
  }

  public int getPathCount() {
    return pathCount;
  }
}
