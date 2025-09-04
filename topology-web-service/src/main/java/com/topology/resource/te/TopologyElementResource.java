package com.topology.resource.te;

import com.topology.dto.TopologyDTO;
import com.topology.primitives.TopologyElement;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.TopologyException;
import com.topology.resource.AbstractResource;
import com.topology.resource.ResourceNaming;
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
