package com.topology.impl.primitives;

import com.topology.primitives.ConnectionPoint;
import com.topology.primitives.NetworkLayer;
import com.topology.primitives.PtpService;
import com.topology.primitives.TopologyManager;

public abstract class PtpServiceImpl extends ServiceImpl implements PtpService{

  private final ConnectionPoint aEnd;
  private final ConnectionPoint zEnd;

  private final boolean directed;

  private final NetworkLayer layer;

  public PtpServiceImpl(TopologyManager manager, int id, ConnectionPoint aEnd, ConnectionPoint zEnd, boolean directed, NetworkLayer layer) {
    super(manager, id);
    this.aEnd = aEnd;
    this.zEnd = zEnd;
    this.directed = directed;
    this.layer = layer;
  }

  @Override
  public ConnectionPoint getaEnd() {
    return aEnd;
  }

  @Override
  public ConnectionPoint getzEnd() {
    return zEnd;
  }

  @Override
  public boolean isDirected() {
    return directed;
  }

  @Override
  public NetworkLayer getLayer() {
    return layer;
  }

}
