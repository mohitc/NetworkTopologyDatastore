package com.topology.resource.conn;

import com.topology.dto.ConnectionDTO;
import com.topology.dto.TopologyDTO;
import com.topology.primitives.Connection;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.TopologyException;
import com.topology.resource.AbstractResource;
import com.topology.resource.ResourceNaming;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.Set;

@Path(ResourceNaming.Connection.PATH)
public class ConnectionResource extends AbstractResource{

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Set<ConnectionDTO> getAllConnections(@PathParam(ResourceNaming.INSTANCE_REF) String instanceID) throws TopologyException {
    TopologyManager manager = getTopologyManager(instanceID);
    Set<Connection> connSet = manager.getAllElements(Connection.class);
    Set<ConnectionDTO> connectionDTOs = new HashSet<>();
    for (Connection conn: connSet) {
      connectionDTOs.add((ConnectionDTO)TopologyDTO.generateDTO(conn));
    }
    return connectionDTOs;
  }

}
