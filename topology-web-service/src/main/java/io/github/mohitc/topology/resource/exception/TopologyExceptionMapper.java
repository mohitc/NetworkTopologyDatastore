package io.github.mohitc.topology.resource.exception;

import io.github.mohitc.topology.primitives.exception.TopologyException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Provider
public class TopologyExceptionMapper implements ExceptionMapper<TopologyException> {

  private static final Logger log = LoggerFactory.getLogger(TopologyExceptionMapper.class);

  @Override
  public Response toResponse(TopologyException e) {
    log.error("Topology Exception", e);
    return Response.serverError().type(MediaType.APPLICATION_JSON).entity(e.getMessage()).build();
  }
}
