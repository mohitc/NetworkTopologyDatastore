package io.github.mohitc.topology.impl.db.primitives;

import io.github.mohitc.topology.primitives.CrossConnect;
import io.github.mohitc.topology.primitives.TopologyManager;
import jakarta.persistence.*;

@Entity
public class CrossConnectDBImpl extends ConnectionDBImpl implements CrossConnect {

  public CrossConnectDBImpl() {
    // Empty constructor to support initialization by persistence.
  }

  public CrossConnectDBImpl(TopologyManager manager, ConnectionPointDBImpl aEnd, ConnectionPointDBImpl zEnd) {
    super(manager, aEnd, zEnd);
  }
}
