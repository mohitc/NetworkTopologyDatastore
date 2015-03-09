package com.topology.resource.exception;

import com.topology.primitives.exception.TopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class TopologyExceptionMapper implements ExceptionMapper<TopologyException> {

  private static final Logger log = LoggerFactory.getLogger(TopologyExceptionMapper.class);

  @Override
  public Response toResponse(TopologyException e) {
    log.error(e.getMessage());
    return Response.serverError().type(MediaType.APPLICATION_JSON).entity(e.getMessage()).build();
  }
}
