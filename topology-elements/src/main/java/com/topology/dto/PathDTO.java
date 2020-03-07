package com.topology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.topology.primitives.Connection;
import com.topology.primitives.Path;
import com.topology.primitives.exception.TopologyException;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PathDTO {

  private boolean strict;

  private boolean directed;

  private int aEndId, zEndId;

  private List<Integer> forwardConnectionSequence, backwardConnectionSequence;

  public PathDTO() {
  }

  public PathDTO(Path path) throws TopologyException {
    if (path == null)
      throw new TopologyException("Path cannot be null");
    this.strict = path.isStrict();
    this.directed = path.isDirected();
    if (path.getaEnd()!=null)
      this.aEndId = path.getaEnd().getID();
    if (path.getzEnd()!=null)
      this.zEndId = path.getzEnd().getID();
    this.forwardConnectionSequence = generateSequence(path.getForwardConnectionSequence());
    this.backwardConnectionSequence = generateSequence(path.getBackwardConnectionSequence());
  }

  private List<Integer> generateSequence(List<Connection> sequence) {
    List<Integer> idSequence = new ArrayList<>();
    if (sequence==null)
      return null;
    for (Connection conn: sequence) {
      idSequence.add(conn.getID());
    }
    return idSequence;
  }

  public boolean isStrict() {
    return strict;
  }

  public void setStrict(boolean strict) {
    this.strict = strict;
  }

  public boolean isDirected() {
    return directed;
  }

  public void setDirected(boolean directed) {
    this.directed = directed;
  }

  public int getaEndId() {
    return aEndId;
  }

  public void setaEndId(int aEndId) {
    this.aEndId = aEndId;
  }

  public int getzEndId() {
    return zEndId;
  }

  public void setzEndId(int zEndId) {
    this.zEndId = zEndId;
  }

  public List<Integer> getForwardConnectionSequence() {
    return forwardConnectionSequence;
  }

  public void setForwardConnectionSequence(List<Integer> forwardConnectionSequence) {
    this.forwardConnectionSequence = forwardConnectionSequence;
  }

  public List<Integer> getBackwardConnectionSequence() {
    return backwardConnectionSequence;
  }

  public void setBackwardConnectionSequence(List<Integer> backwardConnectionSequence) {
    this.backwardConnectionSequence = backwardConnectionSequence;
  }

  public String toString() {
    return "aEnd: " + aEndId + ", zEnd: " + zEndId + "Strict: " + (strict?"true":"false") + ", Directed: " + directed + ", forwardConn: " + forwardConnectionSequence + ", backwardConn: " + backwardConnectionSequence;
  }
}
