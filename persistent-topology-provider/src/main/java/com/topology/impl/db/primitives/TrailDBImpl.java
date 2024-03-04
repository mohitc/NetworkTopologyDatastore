package com.topology.impl.db.primitives;

import com.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import com.topology.primitives.Path;
import com.topology.primitives.Trail;
import com.topology.primitives.exception.TopologyException;
import com.topology.primitives.resource.ConnectionResource;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class TrailDBImpl extends PtpServiceDBImpl implements Trail{

  @OneToOne
  @JoinColumn(name="path_id", referencedColumnName = "id")
  private PathDBImpl path;

  @Override
  public ConnectionResource getReservedResource() {
    return null;
  }

  @Override
  public void setPath(Path path) throws TopologyException {
    if ((path!=null) && (PathDBImpl.class.isAssignableFrom(path.getClass()))) {
      EntityManager em = EntityManagerFactoryHelper.getEntityManager();
      em.getTransaction().begin();
      this.path=(PathDBImpl)path;
      em.getTransaction().commit();
      em.close();
    } else
      throw new TopologyException("Path is null or is not assignable from PathDBImpl class");
  }

  @Override
  public Path getPath() {
    return path;
  }
}
