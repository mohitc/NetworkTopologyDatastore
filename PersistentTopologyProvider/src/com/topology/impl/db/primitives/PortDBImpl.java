package com.topology.impl.db.primitives;

import com.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import com.topology.primitives.ConnectionPoint;
import com.topology.primitives.Port;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQueries({
    @NamedQuery(name=PortDBImpl.GET_CONTAINED_CONNECTION_POINTS_FOR_PORT, query = "SELECT p FROM ConnectionPointDBImpl p WHERE p.parent = :parent")
})
public class PortDBImpl extends ConnectionPointDBImpl implements Port{

  public static final String GET_CONTAINED_CONNECTION_POINTS_FOR_PORT = "GET_CONTAINED_CONNECTION_POINTS_FOR_PORT";

  @Override
  public Set<ConnectionPoint> getContainedConnectionPoints() {
    EntityManager manager = EntityManagerFactoryHelper.getEntityManager();
    Query query = manager.createNamedQuery(PortDBImpl.GET_CONTAINED_CONNECTION_POINTS_FOR_PORT);
    query.setParameter("parent", this);
    Set<ConnectionPoint> resultSet = new HashSet<>();
    resultSet.addAll(query.getResultList());
    return resultSet;
  }
}
