package com.topology.impl.db.primitives;

import com.topology.impl.db.primitives.converters.NetworkLayerConverter;
import com.topology.primitives.ConnectionPoint;
import com.topology.primitives.NetworkLayer;
import com.topology.primitives.PtpService;

import javax.persistence.*;

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
