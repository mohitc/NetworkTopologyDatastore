package com.topology.resource.graph;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CsNodeData {

  private String id;

  private String parent;

  public CsNodeData() {}

  public CsNodeData(String id, String parent) {
    this.id = id;
    this.parent = parent;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }
}
