package io.github.mohitc.topology.impl.db.primitives;

import io.github.mohitc.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import io.github.mohitc.topology.primitives.ConnectionPoint;
import io.github.mohitc.topology.primitives.Port;
import io.github.mohitc.topology.primitives.TopologyManager;

import jakarta.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NamedQueries({
  @NamedQuery(name=PortDBImpl.GET_CONTAINED_CONNECTION_POINTS_FOR_PORT, query = "SELECT p FROM ConnectionPointDBImpl p WHERE p.parent = :parent AND p.topoManagerInstance = :instance")
})
public class PortDBImpl extends ConnectionPointDBImpl implements Port{

  public static final String GET_CONTAINED_CONNECTION_POINTS_FOR_PORT = "GET_CONTAINED_CONNECTION_POINTS_FOR_PORT";

  public PortDBImpl() {
    // Empty constructor to support initialization by persistence.
  }

  public PortDBImpl(TopologyManager manager, TopologyElementDBImpl parent) {
    super(manager, parent);
  }

  @Override
  public Set<ConnectionPoint> getContainedConnectionPoints() {
    try(EntityManager manager = EntityManagerFactoryHelper.getEntityManager()) {
      TypedQuery<ConnectionPointDBImpl> query = manager.createNamedQuery(GET_CONTAINED_CONNECTION_POINTS_FOR_PORT, ConnectionPointDBImpl.class);
      query.setParameter("parent", this);
      query.setParameter("instance", this.topoManagerInstance);
      return query.getResultStream().collect(Collectors.toSet());
    }
  }
}
