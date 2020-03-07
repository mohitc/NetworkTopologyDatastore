package com.topology.resource.te;

import com.topology.dto.TopologyDTO;
import com.topology.primitives.TopologyElement;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.TopologyException;
import com.topology.resource.AbstractResource;
import com.topology.resource.ResourceNaming;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
