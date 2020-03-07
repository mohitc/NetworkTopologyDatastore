package com.topology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.topology.primitives.Link;
import com.topology.primitives.exception.properties.PropertyException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkDTO extends ConnectionDTO {

  public LinkDTO(){
  }

  public LinkDTO(Link link) throws PropertyException {
    this.populateDTO(link);
  }
}
