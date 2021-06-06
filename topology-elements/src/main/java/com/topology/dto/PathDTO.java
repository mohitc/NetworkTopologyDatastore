package com.topology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.topology.primitives.Connection;
import com.topology.primitives.Path;
import com.topology.primitives.exception.TopologyException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PathDTO {

  private boolean strict;

  private boolean directed;

  private int aEndId, zEndId;

  private List<Integer> forwardConnectionSequence, backwardConnectionSequence;

  private List<Integer> forwardConnectionPointSequence, backwardConnectionPointSequence;

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
    if (this.strict) {
      this.forwardConnectionPointSequence = generateConnectionPointSequence(aEndId, path.getForwardConnectionSequence());
      this.backwardConnectionPointSequence = generateConnectionPointSequence(zEndId, path.getBackwardConnectionSequence());
    }
  }

  private List<Integer> generateConnectionPointSequence(int startCpId, List<Connection> sequence) throws TopologyException {
    List<Integer> idSequence = new ArrayList<>();
    idSequence.add(startCpId);
    Iterator<Connection> iter = sequence.iterator();
    while (iter.hasNext()) {
      Connection conn = iter.next();
      int currId = idSequence.get(idSequence.size() - 1);
      if (conn.getaEnd().getID() == currId) {
        idSequence.add(conn.getzEnd().getID());
      } else if (conn.getzEnd().getID() == currId) {
        idSequence.add(conn.getaEnd().getID());
      } else {
        throw new TopologyException("Cannot generate a sequence of connection points with start id " + startCpId +
            "and path " + sequence);
      }
    }
    return idSequence;
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

  public List<Integer> getForwardConnectionPointSequence() {
    return forwardConnectionPointSequence;
  }

  public void setForwardConnectionPointSequence(List<Integer> forwardConnectionPointSequence) {
    this.forwardConnectionPointSequence = forwardConnectionPointSequence;
  }

  public List<Integer> getBackwardConnectionPointSequence() {
    return backwardConnectionPointSequence;
  }

  public void setBackwardConnectionPointSequence(List<Integer> backwardConnectionPointSequence) {
    this.backwardConnectionPointSequence = backwardConnectionPointSequence;
  }

  @Override
  public String toString() {
    return "PathDTO{" +
        "strict=" + strict +
        ", directed=" + directed +
        ", aEndId=" + aEndId +
        ", zEndId=" + zEndId +
        ", forwardConnectionSequence=" + forwardConnectionSequence +
        ", backwardConnectionSequence=" + backwardConnectionSequence +
        ", forwardConnectionPointSequence=" + forwardConnectionPointSequence +
        ", backwardConnectionPointSequence=" + backwardConnectionPointSequence +
        '}';
  }
}
