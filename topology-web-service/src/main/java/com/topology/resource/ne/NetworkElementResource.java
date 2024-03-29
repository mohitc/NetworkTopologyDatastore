package com.topology.resource.ne;

import com.topology.dto.ConnectionPointDTO;
import com.topology.dto.CrossConnectDTO;
import com.topology.dto.LinkDTO;
import com.topology.dto.NetworkElementDTO;
import com.topology.primitives.*;
import com.topology.primitives.exception.TopologyException;
import com.topology.primitives.properties.TEPropertyKey;
import com.topology.resource.AbstractResource;
import com.topology.resource.ResourceNaming;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Path(ResourceNaming.NetworkElement.PATH)
public class NetworkElementResource extends AbstractResource {

  private static final Logger log = LoggerFactory.getLogger(NetworkElementResource.class);

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Set<NetworkElementDTO> getAllNetworkElements(@PathParam(ResourceNaming.INSTANCE_REF) String instanceID) throws TopologyException {
    TopologyManager manager = getTopologyManager(instanceID);
    Set<NetworkElement> nes = manager.getAllElements(NetworkElement.class);
    Set<NetworkElementDTO> neDTOs = new HashSet<>();
    for (NetworkElement ne: nes) {
      neDTOs.add(new NetworkElementDTO(ne));
    }
    return neDTOs;
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  @Path("{neID}")
  public NetworkElementDTO getNeByID(@PathParam(ResourceNaming.INSTANCE_REF) String instanceID, @PathParam("neID") int neID) throws TopologyException {
    TopologyManager manager = getTopologyManager(instanceID);
    NetworkElement ne = manager.getElementByID(neID, NetworkElement.class);
    return new NetworkElementDTO(ne);
  }

  @POST
  @Produces({MediaType.APPLICATION_JSON})
  public NetworkElementDTO createNetworkElement(@PathParam(ResourceNaming.INSTANCE_REF) String instanceID) throws TopologyException {
    TopologyManager manager = getTopologyManager(instanceID);
    NetworkElement ne = manager.createNetworkElement();
    return new NetworkElementDTO(ne);
  }

  @PUT
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes({MediaType.APPLICATION_JSON})
  @Path("{neID}")
  public Response addProperties(@PathParam(ResourceNaming.INSTANCE_REF) String instanceID, @PathParam("neID") int neID, Map <TEPropertyKey, Object> properties) throws TopologyException {
    TopologyManager manager = getTopologyManager(instanceID);
    NetworkElement ne = manager.getElementByID(neID, NetworkElement.class);
    log.info("Adding properties to network element " + ne);
    //TODO add code for revert
    for (Map.Entry<TEPropertyKey, Object> entry : properties.entrySet()) {
      ne.addProperty(entry.getKey(), entry.getValue());
    }
    return Response.ok().build();
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  @Path("{neID}/" + ResourceNaming.NetworkElement.CONNECTION_POINTS)
  public Set<ConnectionPointDTO> getConnectionPoints(@PathParam(ResourceNaming.INSTANCE_REF) String instanceID, @PathParam("neID") int neID) throws TopologyException {
    TopologyManager manager = getTopologyManager(instanceID);
    NetworkElement ne = manager.getElementByID(neID, NetworkElement.class);
    Set<ConnectionPoint> cps = ne.getConnectionPoints(false);
    Set<ConnectionPointDTO> cpDTOs = new HashSet<>();
    for (ConnectionPoint cp: cps) {
      cpDTOs.add(ConnectionPointDTO.createDTO(cp));
    }
    return cpDTOs;
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  @Path("{neID}/" + ResourceNaming.NetworkElement.CROSS_CONNECTS)
  public Set<CrossConnectDTO> getCrossConnects(@PathParam(ResourceNaming.INSTANCE_REF) String instanceID, @PathParam("neID") int neID) throws TopologyException {
    TopologyManager manager = getTopologyManager(instanceID);
    NetworkElement ne = manager.getElementByID(neID, NetworkElement.class);
    Set<CrossConnect> ccs = ne.getAllConnections(CrossConnect.class);
    Set<CrossConnectDTO> ccDTOs = new HashSet<>();
    for (CrossConnect cc: ccs) {
      ccDTOs.add(new CrossConnectDTO(cc));
    }
    return ccDTOs;
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  @Path("{neID}/" + ResourceNaming.NetworkElement.LINKS)
  public Set<LinkDTO> getLinks(@PathParam(ResourceNaming.INSTANCE_REF) String instanceID, @PathParam("neID") int neID) throws TopologyException {
    TopologyManager manager = getTopologyManager(instanceID);
    NetworkElement ne = manager.getElementByID(neID, NetworkElement.class);
    Set<Link> links = ne.getAllConnections(Link.class);
    Set<LinkDTO> linkDTOs = new HashSet<>();
    for (Link link: links) {
      linkDTOs.add(new LinkDTO(link));
    }
    return linkDTOs;
  }

}
