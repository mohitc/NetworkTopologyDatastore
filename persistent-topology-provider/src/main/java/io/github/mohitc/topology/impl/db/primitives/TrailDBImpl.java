package io.github.mohitc.topology.impl.db.primitives;

import io.github.mohitc.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import io.github.mohitc.topology.primitives.Path;
import io.github.mohitc.topology.primitives.Trail;
import io.github.mohitc.topology.primitives.exception.TopologyException;
import io.github.mohitc.topology.primitives.resource.ConnectionResource;

import jakarta.persistence.*;

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
    if (path == null) {
      throw new TopologyException("Path cannot be null.");
    }
    // Use instanceof for cleaner and safer type checking.
    if (!(path instanceof PathDBImpl)) {
      throw new TopologyException("Path is not a manageable database entity (expected PathDBImpl).");
    }

    // The try-with-resources statement ensures the EntityManager is always closed.
    // The explicit 'finally { em.close() }' from the original code was redundant and would cause a compilation error.
    try (EntityManager em = EntityManagerFactoryHelper.getEntityManager()) {
      try {
        em.getTransaction().begin();
        this.path = (PathDBImpl) path;
        // Since this TrailDBImpl instance is likely detached when this method is called,
        // we must merge its state into the persistence context to save the change to the database.
        em.merge(this);
        em.getTransaction().commit();
      } catch (Exception e) {
        // If the transaction is active and an error occurs, it must be rolled back to prevent a corrupt state.
        if (em.getTransaction().isActive()) {
          em.getTransaction().rollback();
        }
        throw new TopologyException("Failed to set and persist path.", e);
      }
    }
  }

  @Override
  public Path getPath() {
    return path;
  }
}
