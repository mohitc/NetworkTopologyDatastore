package com.topology.impl.db.primitives;

import com.topology.primitives.CrossConnect;
import com.topology.primitives.TopologyManager;
import jakarta.persistence.*;

@Entity
public class CrossConnectDBImpl extends ConnectionDBImpl implements CrossConnect {

  public CrossConnectDBImpl() {
  }

  public CrossConnectDBImpl(TopologyManager manager, ConnectionPointDBImpl aEnd, ConnectionPointDBImpl zEnd) {
    super(manager, aEnd, zEnd);
  }
}
