package io.github.mohitc.topology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.mohitc.topology.primitives.CrossConnect;
import io.github.mohitc.topology.primitives.exception.properties.PropertyException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrossConnectDTO extends ConnectionDTO {

  /** Empty constructor for serialization
   *
   */
  public CrossConnectDTO(){
    // Empty constructor to support serialization
  }

  public CrossConnectDTO(CrossConnect cc) throws PropertyException {
    this.populateDTO(cc);
  }
}
