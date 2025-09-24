package io.github.mohitc.topology.impl.db.primitives;

import io.github.mohitc.topology.impl.db.primitives.converters.NetworkLayerConverter;
import io.github.mohitc.topology.primitives.ConnectionPoint;
import io.github.mohitc.topology.primitives.NetworkLayer;
import io.github.mohitc.topology.primitives.PtpService;

import jakarta.persistence.*;

public class PtpServiceDBImpl extends ServiceDBImpl implements PtpService{

  @OneToOne
  @JoinColumn(name="a_end_id", referencedColumnName = "id")
  private ConnectionPointDBImpl aEnd;

  @OneToOne
  @JoinColumn(name="z_end_id", referencedColumnName = "id")
  private ConnectionPointDBImpl zEnd;

  @Column(name="directed")
  private boolean directed;

  @Column(name="layer")
  @Convert(converter= NetworkLayerConverter.class)
  private NetworkLayer layer;

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
