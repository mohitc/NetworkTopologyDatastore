package com.topology.resource.graph;

import com.topology.resource.AbstractResource;
import com.topology.resource.ResourceNaming;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path(ResourceNaming.GRAPH.CS.PATH)
public class CsGraphResource extends AbstractResource {

  private static Map<String, List<CsStyleElement>> csStyle = new HashMap<>();

  @GET
  @Path(ResourceNaming.GRAPH.CS.STYLE)
  @Produces({MediaType.APPLICATION_JSON})
  public List<CsStyleElement> getStyle(@PathParam(ResourceNaming.INSTANCE_REF) String instanceID) {
    if (!csStyle.containsKey(instanceID)) {
      csStyle.put(instanceID, getDefaultCsStyle());
    }
    return csStyle.get(instanceID);

  }

  private List<CsStyleElement> getDefaultCsStyle() {
    List<CsStyleElement> defaultStyle = new ArrayList<>();
    CsStyleElement element = new CsStyleElement();
    element.setSelector("node");
    element.addCssProperty("content", "data(label)");
    element.addCssProperty("text-valign", "center");
    element.addCssProperty("text-halign", "center");
    element.addCssProperty("fontSize", "xx-small");
    defaultStyle.add(element);

    element = new CsStyleElement();
    element.setSelector("$node > node");
    element.addCssProperty("padding-top", "5px");
    element.addCssProperty("padding-left", "5px");
    element.addCssProperty("padding-bottom", "5px");
    element.addCssProperty("padding-right", "5px");
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
