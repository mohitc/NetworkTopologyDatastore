package io.github.mohitc.topology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.mohitc.topology.primitives.NetworkElement;
import io.github.mohitc.topology.primitives.exception.properties.PropertyException;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NetworkElementDTO extends TopologyDTO {
  private Set<Integer> connectionPoints;

  public NetworkElementDTO(){
    // Empty constructor to support serialization
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
