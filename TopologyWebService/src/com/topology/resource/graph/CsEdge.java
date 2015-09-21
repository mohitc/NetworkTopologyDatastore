package com.topology.resource.graph;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class CsEdge extends CsElement {

  private CsEdgeData data;

  public CsEdge() {
    this.setGroup("edges");
  }

  public CsEdgeData getData() {
    return data;
  }

  public void setData(CsEdgeData data) {
    this.data = data;
  }
}
