package com.topology.impl;

import org.eclipse.persistence.logging.SessionLog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;


public class InitDBSchema {

  private Map getPropertiesForSchemaGeneration() {
    Map<String, String> map = new HashMap<>();

    map.put("eclipselink.ddl-generation", "create-tables");
    map.put("eclipselink.create-ddl-jdbc-file-name", "createDDL_ddlGeneration.jdbc");
    map.put("eclipselink.drop-ddl-jdbc-file-name", "dropDDL_ddlGeneration.jdbc");
    map.put("eclipselink.ddl-generation.output-mode", "both");
    map.put("eclipselink.ddl-generation.index-foreign-keys", "true");
    map.put("eclipselink.logging.level", SessionLog.FINE_LABEL);
    return map;
  }

  public void initDbSchema() {
    EntityManagerFactory emf;
    EntityManager em = null;
    try {
      emf = Persistence.createEntityManagerFactory("TopologyProvider", getPropertiesForSchemaGeneration());
      em = emf.createEntityManager();
    } finally {
      if (em!=null)
        em.close();
    }
  }

  public static void main(String[] args) {
    InitDBSchema initDBSchema = new InitDBSchema();
    initDBSchema.initDbSchema();
  }

}
