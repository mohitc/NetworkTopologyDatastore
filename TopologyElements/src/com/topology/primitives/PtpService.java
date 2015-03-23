package com.topology.primitives;

//Representation of a point to point service
public interface PtpService extends Service {

  public ConnectionPoint getaEnd();

  public ConnectionPoint getzEnd();

  public boolean isDirected();

  public NetworkLayer getLayer();

}
