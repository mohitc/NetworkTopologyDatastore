package com.topology.impl.db.primitives;


import com.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;

import javax.persistence.EntityManager;

public class Test {
  public static void main(String[] args) {
    EntityManager em = EntityManagerFactoryHelper.getEntityManager();
  }
}
