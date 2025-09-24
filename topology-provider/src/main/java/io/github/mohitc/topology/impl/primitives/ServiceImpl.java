package io.github.mohitc.topology.impl.primitives;

import io.github.mohitc.topology.primitives.Service;
import io.github.mohitc.topology.primitives.TopologyManager;

public abstract class ServiceImpl extends TopologyElementImpl implements Service {

  public ServiceImpl(TopologyManager manager, int id) {
    super(manager, id);
  }
}
