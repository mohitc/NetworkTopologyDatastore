package com.topology.impl.db.primitives;

import com.topology.primitives.Link;
import com.topology.primitives.TopologyManager;

import javax.persistence.Entity;

@Entity
public class LinkDBImpl extends ConnectionDBImpl implements Link {

  public LinkDBImpl() {
  }

  public LinkDBImpl(TopologyManager manager, ConnectionPointDBImpl aEnd, ConnectionPointDBImpl zEnd) {
    super(manager, aEnd, zEnd);
  }
}
