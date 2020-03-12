package com.topology.impl.primitives.manager;

import com.topology.impl.db.primitives.TopologyManagerFactoryDBImpl;
import com.topology.impl.primitives.TopologyManagerFactoryImpl;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.TopologyManagerFactory;
import com.topology.primitives.exception.TopologyException;
import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.impl.DoubleConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class TopoManagerHelper {

  private static final Logger log = LoggerFactory.getLogger(TopoManagerHelper.class);

  private static final TopologyManagerFactory factory;

  private static final String defaultTopoManagerId = "123";

  private static final boolean useMemManager;

  static {
    boolean tempUseMemManager;
    String filename = "config.properties";
    try (InputStream resourceStream = TopoManagerHelper.class.getClassLoader().getResourceAsStream("config.properties")) {
      Properties props = new Properties();
      props.load(resourceStream);
      String propertyFromFile = props.getProperty("provider", "persistent-topology-provider").trim();
      tempUseMemManager = propertyFromFile.equalsIgnoreCase("topology-provider");
      log.info("Provider given in file = {}", props.getProperty("provider"));
    } catch (Exception e) {
      log.error("Error loading configuration file", e);
      tempUseMemManager=true;
    }
    useMemManager=tempUseMemManager;
    factory = useMemManager ? new TopologyManagerFactoryImpl() : new TopologyManagerFactoryDBImpl();
    log.info("Using Factory = {}", factory.getClass().getSimpleName());
  }

  public static TopologyManagerFactory getFactoryInstance() {
    return factory;
  }

  public static TopologyManager getInstance() {
    try {
      if (factory.hasTopologyManager(defaultTopoManagerId)) {
        return factory.getTopologyManager(defaultTopoManagerId);
      } else {
        TopologyManager manager = factory.createTopologyManager(defaultTopoManagerId);
        try {
          if (!manager.containsKey("X"))
            manager.registerKey("X", "X coordinate", Double.class, DoubleConverter.class);
          if (!manager.containsKey("Y"))
            manager.registerKey("Y", "Y coordinate", Double.class, DoubleConverter.class);
        } catch (PropertyException e) {
          log.error("Error in registering property", e);
        }
        return manager;
      }
    } catch (TopologyException e) {
      log.error("Error in creating a topology manager from factory implementation", e);
    }
    return null;
  }

}
