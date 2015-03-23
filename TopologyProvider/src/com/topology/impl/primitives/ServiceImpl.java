package com.topology.impl.primitives;

import com.topology.primitives.Service;
import com.topology.primitives.TopologyManager;

public abstract class ServiceImpl extends TopologyElementImpl implements Service {

  public ServiceImpl(TopologyManager manager, int id) {
    super(manager, id);
  }
}
