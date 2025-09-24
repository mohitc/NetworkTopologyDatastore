package io.github.mohitc.topology.resource;

import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import io.github.mohitc.topology.resource.manager.TopologyManagerFactoryHelper;

public abstract class AbstractResource {

  protected TopologyManager getTopologyManager(String instanceID) throws TopologyException {
    return TopologyManagerFactoryHelper.getInstance().getTopologyManager(instanceID);
  }


}
