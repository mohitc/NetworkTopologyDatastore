package com.topology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.topology.primitives.CrossConnect;
import com.topology.primitives.exception.properties.PropertyException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrossConnectDTO extends ConnectionDTO {

  public CrossConnectDTO(){
  }

  public CrossConnectDTO(CrossConnect cc) throws PropertyException {
    this.populateDTO(cc);
  }
}
