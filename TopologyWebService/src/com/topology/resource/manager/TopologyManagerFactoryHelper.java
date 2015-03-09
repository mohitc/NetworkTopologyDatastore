package com.topology.resource.manager;

import com.topology.impl.primitives.TopologyManagerFactoryImpl;
import com.topology.primitives.TopologyManagerFactory;

public class TopologyManagerFactoryHelper {

  private static TopologyManagerFactory _instance;

  public static TopologyManagerFactory getInstance() {
    if (_instance==null)
      _instance = new TopologyManagerFactoryImpl();
    return _instance;
  }
}
