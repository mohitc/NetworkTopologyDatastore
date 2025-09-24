package io.github.mohitc.topology.impl.primitives;

import io.github.mohitc.topology.primitives.ConnectionPoint;
import io.github.mohitc.topology.primitives.NetworkLayer;
import io.github.mohitc.topology.primitives.PtpService;
import io.github.mohitc.topology.primitives.TopologyManager;

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
