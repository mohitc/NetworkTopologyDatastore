package com.topology.dto;

import com.topology.primitives.Connection;
import com.topology.primitives.NetworkLayer;
import com.topology.primitives.exception.properties.PropertyException;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public abstract class ConnectionDTO extends TopologyDTO {

  private Integer aEnd, zEnd;

  private boolean directed;

  private NetworkLayer layer;

  public <T extends Connection> void populateDTO(T conn) throws PropertyException {
    if (conn!=null) {
      this.aEnd = (conn.getaEnd()!=null)?conn.getaEnd().getID():null;
      this.zEnd = (conn.getzEnd()!=null)?conn.getzEnd().getID():null;
      this.directed = conn.isDirected();
      this.layer = conn.getLayer();
      super.populateDTO(conn);
      //TODO include DTO definition for connection resource
    }
  }

  public NetworkLayer getLayer() {
    return layer;
  }

  public void setLayer(NetworkLayer layer) {
    this.layer = layer;
  }

  public boolean isDirected() {
    return directed;
  }

  public void setDirected(boolean directed) {
    this.directed = directed;
  }

  public Integer getzEnd() {
    return zEnd;
  }

  public void setzEnd(Integer zEnd) {
    this.zEnd = zEnd;
  }

  public Integer getaEnd() {
    return aEnd;
  }

  public void setaEnd(Integer aEnd) {
    this.aEnd = aEnd;
  }
}
