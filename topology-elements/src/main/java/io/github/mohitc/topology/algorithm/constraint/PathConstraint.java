package io.github.mohitc.topology.algorithm.constraint;

public class PathConstraint {

  //Boolean to indicate that path is directed
  private final boolean directed;

  //Boolean to indicate that path is symmetric
  private final boolean symmetric;

  //integer to indicate number of paths
  private final int pathCount;

  public PathConstraint(boolean directed, boolean symmetric) {
    this(directed, symmetric, 1);
  }

  public PathConstraint(boolean directed, boolean symmetric, int pathCount) {
    this.directed = directed;
    this.symmetric = symmetric;
    this.pathCount = pathCount;
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
