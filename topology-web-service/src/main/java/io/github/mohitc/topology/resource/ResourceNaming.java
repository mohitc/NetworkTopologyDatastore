package io.github.mohitc.topology.resource;

/**
 * A utility class that holds constant string values for REST API resource paths.
 * <p>
 * This class is declared as {@code final} with a private constructor to prevent it
 * from being instantiated or extended, following the utility class pattern.
 */
public final class ResourceNaming {

  private ResourceNaming() {
    // Prevent instantiation
  }

  public static final String INSTANCE_REF = "instance";

  public static final class NetworkElement {
    private NetworkElement() {}
    public static final String PATH = TE.PATH + "/nodes";
    public static final String CONNECTION_POINTS = "connectionpoints";
    public static final String CROSS_CONNECTS = "crossconnects";
    public static final String LINKS = "links";
    public static final String TRAILS = "trails";
  }

  public static final class TE {
    private TE() {}
    public static final String PATH = "{" + INSTANCE_REF + "}";
  }

  public static final class Connection {
    private Connection() {}
    public static final String PATH = TE.PATH + "/connections";
  }

  public static final class ConnectionPoint {
    private ConnectionPoint() {}
    public static final String PATH = "connectionpoints";
  }

  public static final class Graph {
    private Graph() {}
    public static final String PATH = "graph";
    public static final String D3PATH = TE.PATH + "/graph/d3/";
  }

  public static final class GRAPH {
    private GRAPH() {}
    public static final class CS {
      private CS() {}
      public static final String PATH = TE.PATH + "/graph/cs/";
      public static final String STYLE = "style";
    }
  }
}
