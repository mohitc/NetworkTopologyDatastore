package com.topology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.topology.primitives.ConnectionPoint;
import com.topology.primitives.Port;
import com.topology.primitives.exception.properties.PropertyException;

import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortDTO extends ConnectionPointDTO {

  private Set<Integer> containedCPs;

  public PortDTO(){
  }

  public PortDTO(Port port) throws PropertyException {
    this.populateDTO(port);
  }

  public void populateDTO(Port port) throws PropertyException {
    if (port!=null) {
      if (port.getContainedConnectionPoints()!=null) {
        Set<ConnectionPoint> cps = port.getContainedConnectionPoints();
        this.setContainedCPs(generateSet(cps));
      } else {
        this.setContainedCPs(new HashSet<>());
      }
      super.populateDTO(port);
    }
  }

  public Set<Integer> getContainedCPs() {
    return containedCPs;
  }

  public void setContainedCPs(Set<Integer> containedCPs) {
    this.containedCPs = containedCPs;
  }
}
