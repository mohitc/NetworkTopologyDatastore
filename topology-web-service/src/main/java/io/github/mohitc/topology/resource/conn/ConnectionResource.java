package io.github.mohitc.topology.resource.conn;

import io.github.mohitc.topology.dto.ConnectionDTO;
import io.github.mohitc.topology.dto.TopologyDTO;
import io.github.mohitc.topology.primitives.Connection;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import io.github.mohitc.topology.resource.AbstractResource;
import io.github.mohitc.topology.resource.ResourceNaming;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
