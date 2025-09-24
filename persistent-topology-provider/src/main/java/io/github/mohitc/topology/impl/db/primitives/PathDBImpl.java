package io.github.mohitc.topology.impl.db.primitives;

import io.github.mohitc.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import io.github.mohitc.topology.primitives.Connection;
import io.github.mohitc.topology.primitives.ConnectionPoint;
import io.github.mohitc.topology.primitives.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.List;

@Entity
@Table(name="path")
public class PathDBImpl implements Path {

  private static final Logger log = LoggerFactory.getLogger(PathDBImpl.class);

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy=GenerationType.AUTO)
  private int id;

  @OneToOne
  @JoinColumn(name="a_end_id", referencedColumnName = "id")
  private ConnectionPointDBImpl aEnd;

  @OneToOne
  @JoinColumn(name="z_end_id", referencedColumnName = "id")
  private ConnectionPointDBImpl zEnd;

  @Column(name="strict")
  private boolean strict;

  @Column(name="directed")
  private boolean directed;

  @Override
  public boolean isStrict() {
    return strict;
  }

  @Override
  public void setStrict(boolean strict) {
    try(EntityManager em = EntityManagerFactoryHelper.getEntityManager()) {
      em.getTransaction().begin();
      this.strict = strict;
      em.getTransaction().commit();
    }
  }

  @Override
  public boolean isDirected() {
    return directed;
  }

  @Override
  public void setDirected(boolean directed) {
    try(EntityManager em = EntityManagerFactoryHelper.getEntityManager()) {
      em.getTransaction().begin();
      this.directed = directed;
      em.getTransaction().commit();
    }
  }

  @Override
  public List<Connection> getForwardConnectionSequence() {
    // TODO not implemented
    return Collections.emptyList();
  }

  @Override
  public void setForwardConnectionSequence(List<Connection> sequence) {
    //TODO not implemented
  }

  @Override
  public List<Connection> getBackwardConnectionSequence() {
    //TODO not implemented
    return Collections.emptyList();
  }

  @Override
  public void setBackwardConnectionSequence(List<Connection> sequence) {
    //TODO not implemented
  }

  @Override
  public ConnectionPoint getaEnd() {
    return aEnd;
  }

  @Override
  public void setaEnd(ConnectionPoint aEnd) {
    if (aEnd!=null && ConnectionPointDBImpl.class.isAssignableFrom(aEnd.getClass())) {
      try(EntityManager em = EntityManagerFactoryHelper.getEntityManager()) {
        em.getTransaction().begin();
        this.aEnd = (ConnectionPointDBImpl) aEnd;
        em.getTransaction().commit();
      }
    } else {
      log.error("Aend Connection Point is either null or is not an instance of ConnectionPointDBImpl");
    }
  }

  @Override
  public ConnectionPoint getzEnd() {
    return zEnd;
  }

  @Override
  public void setzEnd(ConnectionPoint zEnd) {
    if (zEnd!=null && ConnectionPointDBImpl.class.isAssignableFrom(zEnd.getClass())) {
      try(EntityManager em = EntityManagerFactoryHelper.getEntityManager()) {
        em.getTransaction().begin();
        this.zEnd = (ConnectionPointDBImpl) zEnd;
        em.getTransaction().commit();
      }
    } else {
      log.error("Zend Connection Point is either null or is not an instance of ConnectionPointDBImpl");
    }

  }
}
