package com.topology.impl.db.primitives;


import com.topology.primitives.TopologyManager;
import com.topology.primitives.TopologyManagerFactory;
import com.topology.primitives.exception.TopologyException;

public class TopologyManagerFactoryDBImpl implements TopologyManagerFactory{

  @Override
  public TopologyManager createTopologyManager(String id) throws TopologyException {
    return new TopologyManagerDBImpl("id");
  }

  @Override
  public TopologyManager getTopologyManager(String id) throws TopologyException {
    return new TopologyManagerDBImpl("id");
  }

  @Override
  public boolean hasTopologyManager(String id) {
    return true;
  }

  @Override
  public void removeTopologyManager(String id) throws TopologyException {
    //do nothing
  }
}
