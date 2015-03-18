package com.topology.resource;

import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.TopologyException;
import com.topology.resource.manager.TopologyManagerFactoryHelper;

public abstract class AbstractResource {

  protected TopologyManager getTopologyManager(String instanceID) throws TopologyException {
    return TopologyManagerFactoryHelper.getInstance().getTopologyManager(instanceID);
  }


}
