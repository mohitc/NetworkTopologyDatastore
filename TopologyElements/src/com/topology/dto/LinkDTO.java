package com.topology.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.topology.primitives.Link;
import com.topology.primitives.exception.properties.PropertyException;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class LinkDTO extends ConnectionDTO {

  public LinkDTO(){
  }

  public LinkDTO(Link link) throws PropertyException {
    this.populateDTO(link);
  }
}
