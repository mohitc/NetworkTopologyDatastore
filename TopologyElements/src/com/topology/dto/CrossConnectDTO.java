package com.topology.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.topology.primitives.CrossConnect;
import com.topology.primitives.exception.properties.PropertyException;

@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class CrossConnectDTO extends ConnectionDTO {

  public CrossConnectDTO(){
  }

  public CrossConnectDTO(CrossConnect cc) throws PropertyException {
    this.populateDTO(cc);
  }
}
