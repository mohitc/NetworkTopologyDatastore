package com.topology.transfer;

import com.topology.primitives.Trail;
import com.topology.primitives.exception.TopologyException;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class TrailDTO extends ConnectionDTO {

  private PathDTO path;

  public TrailDTO(){
  }

  public TrailDTO(Trail trail) throws TopologyException {
    super.populateDTO(trail);
    this.path = new PathDTO(trail.getPath());
  }

  public PathDTO getPath() {
    return path;
  }

  public void setPath(PathDTO path) {
    this.path = path;
  }
}
