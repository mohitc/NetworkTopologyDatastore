package io.github.mohitc.topology.impl.db.primitives;

import io.github.mohitc.topology.primitives.Link;
import io.github.mohitc.topology.primitives.TopologyManager;
import jakarta.persistence.*;

@Entity
public class LinkDBImpl extends ConnectionDBImpl implements Link {

  public LinkDBImpl() {
    // Empty constructor to support initialization by persistence.
  }

  public LinkDBImpl(TopologyManager manager, ConnectionPointDBImpl aEnd, ConnectionPointDBImpl zEnd) {
    super(manager, aEnd, zEnd);
  }
}
