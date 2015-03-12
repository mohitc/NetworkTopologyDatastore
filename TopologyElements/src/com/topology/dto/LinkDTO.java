package com.topology.dto;

import com.topology.primitives.Link;
import com.topology.primitives.exception.properties.PropertyException;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class LinkDTO extends ConnectionDTO {

  public LinkDTO(){
  }

  public LinkDTO(Link link) throws PropertyException {
    this.populateDTO(link);
  }
}
