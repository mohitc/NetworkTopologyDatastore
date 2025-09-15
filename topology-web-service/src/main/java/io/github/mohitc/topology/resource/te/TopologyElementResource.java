package io.github.mohitc.topology.resource.te;

import io.github.mohitc.topology.dto.TopologyDTO;
import io.github.mohitc.topology.primitives.TopologyElement;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import io.github.mohitc.topology.resource.AbstractResource;
import io.github.mohitc.topology.resource.ResourceNaming;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path(ResourceNaming.TE.PATH)
public class TopologyElementResource extends AbstractResource {

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  @Path("{id}")
  public TopologyDTO getElementByID(@PathParam(ResourceNaming.INSTANCE_REF) String instanceID, @PathParam("id") int teID)throws TopologyException {
    TopologyManager manager = getTopologyManager(instanceID);
    TopologyElement te = manager.getElementByID(teID);
    return TopologyDTO.generateDTO(te);
  }
}
