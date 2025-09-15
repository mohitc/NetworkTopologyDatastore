package io.github.mohitc.topology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.mohitc.topology.primitives.Trail;
import io.github.mohitc.topology.primitives.exception.TopologyException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrailDTO extends ConnectionDTO {

  private PathDTO path;

  public TrailDTO(){
    // Empty constructor to support serialization
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
