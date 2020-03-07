package com.topology.primitives;

//Representation of a point to point service
public interface PtpService extends Service {

  ConnectionPoint getaEnd();

  ConnectionPoint getzEnd();

  boolean isDirected();

  NetworkLayer getLayer();

}
