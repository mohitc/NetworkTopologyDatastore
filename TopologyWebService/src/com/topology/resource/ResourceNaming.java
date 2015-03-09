package com.topology.resource;

public interface ResourceNaming {

  public static final String INSTANCE_REF = "instance";

  public static interface NetworkElement {
    public static final String PATH = "{" + INSTANCE_REF +"}/nodes";
    public static final String CONNECTION_POINTS = "connectionpoints";
    public static final String CROSS_CONNECTS = "crossconnects";
    public static final String LINKS = "links";
    public static final String TRAILS = "trails";
  }

  public static interface ConnectionPoint {
    public static final String PATH = "connectionpoints";
  }
}
