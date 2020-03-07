package com.topology.impl.db;

import com.topology.impl.InitDBSchema;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.fail;

public class InitDbSchemaTest {

  private final Logger log = LoggerFactory.getLogger(InitDbSchemaTest.class);

  @Test
  public void initDbSchemaTest() {
    InitDBSchema initDBSchema = new InitDBSchema();
    try {
      initDBSchema.initDbSchema();
    } catch (Exception e) {
      log.error("Error while initializing DB schema", e);
      fail("Database schema could not be initialized");
    }
  }
}
