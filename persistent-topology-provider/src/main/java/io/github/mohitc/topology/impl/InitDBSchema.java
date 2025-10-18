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

  private Map<String, String> getPropertiesForSchemaGeneration(boolean createInDatabase) {
    Map<String, String> map = new HashMap<>();

    // This property tells EclipseLink to generate DDL for creating tables.
    map.put("eclipselink.ddl-generation", "create-tables");

    // Configure the output file names for the generated DDL.
    map.put("eclipselink.create-ddl-jdbc-file-name", "createDDL_ddlGeneration.jdbc");
    map.put("eclipselink.drop-ddl-jdbc-file-name", "dropDDL_ddlGeneration.jdbc");

    // Control where the DDL is sent: to a file, to the database, or both.
    if (createInDatabase) {
      log.info("Configuring DDL generation to create tables in the database and write to SQL scripts.");
      map.put("eclipselink.ddl-generation.output-mode", "both");
    } else {
      log.info("Configuring DDL generation to write to SQL scripts only.");
      map.put("eclipselink.ddl-generation.output-mode", "sql-script");
    }

    map.put("eclipselink.ddl-generation.index-foreign-keys", "true");
    map.put("eclipselink.logging.level", SessionLog.FINE_LABEL);
    return map;
  }

  public void initDbSchema(boolean createInDatabase, Map<String, String> runtimeProperties) {
    Map<String, String> effectiveProperties = getPropertiesForSchemaGeneration(createInDatabase);
    if (runtimeProperties != null) {
      effectiveProperties.putAll(runtimeProperties);
    }
    try (EntityManagerFactory emf = Persistence
      .createEntityManagerFactory("TopologyProvider", effectiveProperties);
         EntityManager em = emf.createEntityManager()) {
      log.info("Schema generation process completed successfully.");
      // The EntityManagerFactory creation triggers the DDL generation.
      // The try-with-resources block ensures both emf and em are closed.
      log.info("Created Entity manager: {}", em);
    } catch (Exception e) {
      log.error("Error while initializing DB schema", e);
    }
  }
}
