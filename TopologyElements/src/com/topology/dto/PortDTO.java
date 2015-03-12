package com.topology.dto;

import com.topology.primitives.ConnectionPoint;
import com.topology.primitives.Port;
import com.topology.primitives.exception.properties.PropertyException;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Set;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
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
