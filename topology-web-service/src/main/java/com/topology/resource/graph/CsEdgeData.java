package com.topology.resource.graph;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CsEdgeData {

  private String id;

  private String source;

  private String target;

  /**
   * Default Constructor required for JSON Serialization
   */
  public CsEdgeData() {}

  public CsEdgeData(String id, String source, String target) {
    this.id = id;
    this.source = source;
    this.target = target;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }
}
