package com.topology.resource.graph;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
