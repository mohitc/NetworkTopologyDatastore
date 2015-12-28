package com.topology.resource;

public interface ResourceNaming {

  String INSTANCE_REF = "instance";

  interface NetworkElement {
    String PATH = TE.PATH + "/nodes";
    String CONNECTION_POINTS = "connectionpoints";
    String CROSS_CONNECTS = "crossconnects";
    String LINKS = "links";
    String TRAILS = "trails";
  }

  interface TE {
    String PATH = "{" + INSTANCE_REF +"}";
  }

  interface Connection {
    String PATH = TE.PATH + "/connections";
  }
  interface ConnectionPoint {
    String PATH = "connectionpoints";
  }

  interface Graph {
    String PATH = "graph";
    String D3PATH = TE.PATH + "/graph/d3/";
  }

  interface GRAPH {
    interface CS {
      String PATH = "{" + INSTANCE_REF +"}/graph/cs/";

      String STYLE = "style";
    }
  }
}
