package io.github.mohitc.topology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.mohitc.topology.primitives.ConnectionPoint;
import io.github.mohitc.topology.primitives.Port;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import io.github.mohitc.topology.primitives.exception.properties.PropertyException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectionPointDTO extends TopologyDTO {

  private Integer parentID;

  public ConnectionPointDTO(){
    // Empty constructor to support serialization
  }

  public ConnectionPointDTO(ConnectionPoint cp) throws PropertyException {
    this.populateDTO(cp);
  }

  public int getParentID() {
    return parentID;
  }

  public void setParentID(int parentID) {
    this.parentID = parentID;
  }

  public <T extends ConnectionPoint> void populateDTO(T cp) throws PropertyException {
    if (cp!=null) {
      if (cp.getParent()!=null) {
        this.parentID = cp.getParent().getID();
      } else {
        this.parentID = -1;
      }
      super.populateDTO(cp);
    }
  }

  public static ConnectionPointDTO createDTO(ConnectionPoint cp) throws TopologyException {
    if (cp!=null) {
      if (Port.class.isAssignableFrom(cp.getClass())) {
        return new PortDTO((Port) cp);
      } else {
        return new ConnectionPointDTO(cp);
      }
    }
    throw new TopologyException("Provided connection point cannot be null");
  }
}
