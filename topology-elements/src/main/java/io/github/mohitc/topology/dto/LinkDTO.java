package io.github.mohitc.topology.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.mohitc.topology.primitives.Link;
import io.github.mohitc.topology.primitives.exception.properties.PropertyException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkDTO extends ConnectionDTO {

  /** Empty constructor for serialization
   *
   */
  public LinkDTO(){
    // Empty constructor to support serialization
  }

  public LinkDTO(Link link) throws PropertyException {
    this.populateDTO(link);
  }
}
