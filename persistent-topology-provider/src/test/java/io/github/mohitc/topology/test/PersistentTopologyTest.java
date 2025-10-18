package io.github.mohitc.topology.test;

import io.github.mohitc.topology.impl.InitDBSchema;
import io.github.mohitc.topology.impl.db.persistencehelper.EntityManagerFactoryHelper;
import io.github.mohitc.topology.impl.db.primitives.TopologyManagerFactoryDBImpl;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.TopologyManagerFactory;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersistentTopologyTest extends AbstractTopologyTest {

  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PersistentTopologyTest.class);

  private PostgreSQLContainer<?> postgreSQLContainer;

  private final Object lock = new Object();

  private TopologyManagerFactory factory;

  private TopologyManager manager;

  @Override
  public void init() {
    postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
      .withDatabaseName("topology-db")
      .withUsername("sa")
      .withPassword("sa");
    postgreSQLContainer.start();
    Map<String, String> properties = new HashMap<>();
    properties.put("jakarta.persistence.jdbc.url", postgreSQLContainer.getJdbcUrl());
    properties.put("jakarta.persistence.jdbc.user", postgreSQLContainer.getUsername());
    properties.put("jakarta.persistence.jdbc.password", postgreSQLContainer.getPassword());
    log.info("Properties: URL: {} user: {}, password: {}", postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());

    // Pass the dynamic properties to the schema initializer.
    new InitDBSchema().initDbSchema(true, properties);
    EntityManagerFactoryHelper.init(properties);
  }

  @Override
  public TopologyManagerFactory getTopologyManagerFactory() {
    if (factory == null) {
      synchronized (lock) {
        factory = new TopologyManagerFactoryDBImpl();
      }
    }
    return factory;
  }

  @Override
  public TopologyManager getTopologyManager() {
    if (manager == null) {
      synchronized (lock) {
        try {
          manager = getTopologyManagerFactory().createTopologyManager("default");
        } catch (Exception e) {
          throw new RuntimeException("Error while creating topology manager", e);
        }
      }
    }
    return manager;
  }

  @Override
  public void close() {
    EntityManagerFactoryHelper.close();
    postgreSQLContainer.stop();
  }
}
