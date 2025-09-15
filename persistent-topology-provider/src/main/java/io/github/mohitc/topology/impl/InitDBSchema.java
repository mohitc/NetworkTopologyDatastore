package io.github.mohitc.topology.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.eclipse.persistence.logging.SessionLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class InitDBSchema {

  private static final Logger log = LoggerFactory.getLogger(InitDBSchema.class);

  private Map<String, String> getPropertiesForSchemaGeneration() {
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
    try (EntityManagerFactory emf = Persistence
      .createEntityManagerFactory("TopologyProvider", getPropertiesForSchemaGeneration());
         EntityManager em = emf.createEntityManager()) {
      log.info("Successfully Initialized entityManager: {}", em);
      // The EntityManager is now automatically closed
    } catch (Exception e) {
      log.error("Error while initializing DB schema", e);
    }
  }

  public static void main(String[] args) {
    InitDBSchema initDBSchema = new InitDBSchema();
    initDBSchema.initDbSchema();
  }

}
