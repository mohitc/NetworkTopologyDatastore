package io.github.mohitc.topology.resource.graph;

import io.github.mohitc.topology.primitives.*;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import io.github.mohitc.topology.primitives.exception.properties.PropertyException;
import io.github.mohitc.topology.primitives.properties.TEPropertyKey;
import io.github.mohitc.topology.resource.AbstractResource;
import io.github.mohitc.topology.resource.ResourceNaming;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Path(ResourceNaming.GRAPH.CS.PATH)
public class CsGraphResource extends AbstractResource {

  private static final Logger log = LoggerFactory.getLogger(CsGraphResource.class);

  private static final Map<String, List<CsStyleElement>> csStyle = new HashMap<>();

  @GET
  @Path(ResourceNaming.GRAPH.CS.STYLE)
  @Produces({MediaType.APPLICATION_JSON})
  public List<CsStyleElement> getStyle(@PathParam(ResourceNaming.INSTANCE_REF) String instanceID) {
    if (!csStyle.containsKey(instanceID)) {
      csStyle.put(instanceID, getDefaultCsStyle());
    }
    return csStyle.get(instanceID);
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public List<CsElement> getGraphModel(@PathParam(ResourceNaming.INSTANCE_REF) String instanceID) throws TopologyException {

    List<CsNode> nodes = new ArrayList<>();
    TopologyManager manager = getTopologyManager(instanceID);
    Set<NetworkElement> nes = manager.getAllElements(NetworkElement.class);
    for (NetworkElement ne: nes) {
      nodes.addAll(populateNode(ne));
    }
    List<CsElement> out = new ArrayList<>(nodes);
    Set<Connection> conns = manager.getAllElements(Connection.class);
    for (Connection conn: conns) {
      out.add(populateEdge(conn));
    }
    return out;
  }

  private CsEdge populateEdge(Connection conn) {
    CsEdge edge = new CsEdge();
    CsEdgeData data = new CsEdgeData(Integer.toString(conn.getID()), Integer.toString(conn.getaEnd().getID()), Integer.toString(conn.getzEnd().getID()));
    edge.setData(data);
    return edge;
  }


  private List<CsNode> populateNode(TopologyElement te) {
    List<CsNode> out = new ArrayList<>();
    try {
      TEPropertyKey xCoordinate = te.getTopologyManager().getKey("X");
      TEPropertyKey yCoordinate = te.getTopologyManager().getKey("Y");

      if (te instanceof NetworkElement ne) {
        //generate parent node
        CsNode node = new CsNode();
        node.setData(new CsNodeData(Integer.toString(te.getID()), null));
        try {
          CsNodePosition pos = new CsNodePosition(te.getProperty(xCoordinate, Double.class), te.getProperty(yCoordinate, Double.class));
          node.setPosition(pos);
        } catch (PropertyException e) {
          log.info("Position not available, not initializing coordinates");
          //position not available, not initializing;
        }
        out.add(node);
        Set<ConnectionPoint> cps = ne.getConnectionPoints(false);
        for (ConnectionPoint cp : cps) {
          out.addAll(populateNode(cp));
        }
      } else if (te instanceof ConnectionPoint cp) {
        //generate parent node
        CsNode node = new CsNode();
        TopologyElement parent = cp.getParent();
        node.setData(new CsNodeData(Integer.toString(te.getID()), (parent != null) ? Integer.toString(parent.getID()) : null));
        try {
          CsNodePosition pos = new CsNodePosition(te.getProperty(xCoordinate, Double.class), te.getProperty(yCoordinate, Double.class));
          node.setPosition(pos);
        } catch (PropertyException e) {
          log.info("Position not available, not initializing coordinates");
          //position not available, not initializing;
        }
        out.add(node);
        if (cp instanceof Port) {
          Set<ConnectionPoint> cps = ((Port) cp).getContainedConnectionPoints();
          for (ConnectionPoint points : cps) {
            out.addAll(populateNode(points));
          }
        }
      }
    } catch (PropertyException e) {
      log.error("Coordinates  not initialized in Topology Manager", e);
    }
    return out;
  }


  private List<CsStyleElement> getDefaultCsStyle() {
    List<CsStyleElement> defaultStyle = new ArrayList<>();
    CsStyleElement element = new CsStyleElement();
    element.setSelector("node");
    element.addCssProperty("content", "data(id)");
    element.addCssProperty("text-valign", "center");
    element.addCssProperty("text-halign", "center");
    element.addCssProperty("fontSize", "xx-small");
    element.addCssProperty("background-color", "red");
    defaultStyle.add(element);

    element = new CsStyleElement();
    element.setSelector("$node > node");
    element.addCssProperty("padding-top", "1px");
    element.addCssProperty("padding-left", "1px");
    element.addCssProperty("padding-bottom", "1px");
    element.addCssProperty("padding-right", "1px");
    element.addCssProperty("text-valign", "top");
    element.addCssProperty("text-halign", "center");
    element.addCssProperty("fontSize", "xx-small");
    defaultStyle.add(element);


    element = new CsStyleElement();
    element.setSelector("edge");
    element.addCssProperty("target-arrow-shape", "triangle");
    defaultStyle.add(element);
    element = new CsStyleElement();
    element.setSelector("selected");

    element.addCssProperty("background-color", "black");
    element.addCssProperty("line-color", "black");
    element.addCssProperty("target-arrow-color", "black");
    element.addCssProperty("source-arrow-color", "black");

    return defaultStyle;
  }
}
