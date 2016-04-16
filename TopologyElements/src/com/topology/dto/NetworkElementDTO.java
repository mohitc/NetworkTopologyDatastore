package com.topology.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.topology.primitives.NetworkElement;
import com.topology.primitives.exception.properties.PropertyException;

import java.util.Set;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class NetworkElementDTO extends TopologyDTO {
  private Set<Integer> connectionPoints;

  public NetworkElementDTO(){
  }

  public NetworkElementDTO(NetworkElement ne) throws PropertyException {
    this.populateDTO(ne);
  }

  public void populateDTO(NetworkElement ne) throws PropertyException {
    if (ne!=null) {
      super.populateDTO(ne);
      this.setConnectionPoints(generateSet(ne.getConnectionPoints(false)));
    }
  }

  public Set<Integer> getConnectionPoints() {
    return connectionPoints;
  }

  public void setConnectionPoints(Set<Integer> connectionPoints) {
    this.connectionPoints = connectionPoints;
  }
}
