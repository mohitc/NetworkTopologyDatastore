package com.topology.dto;

import com.topology.primitives.CrossConnect;
import com.topology.primitives.exception.properties.PropertyException;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class CrossConnectDTO extends ConnectionDTO {

  public CrossConnectDTO(){
  }

  public CrossConnectDTO(CrossConnect cc) throws PropertyException {
    this.populateDTO(cc);
  }
}
