package test.topology.primitives.manager;

import com.topology.impl.db.primitives.TopologyManagerFactoryDBImpl;
import com.topology.impl.primitives.TopologyManagerFactoryImpl;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.TopologyManagerFactory;
import com.topology.primitives.exception.TopologyException;
import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.impl.DoubleConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopoManagerHelper {

  private static final Logger log = LoggerFactory.getLogger(TopoManagerHelper.class);

  private static TopologyManagerFactory factory = new TopologyManagerFactoryImpl();
  private static TopologyManagerFactory dbfactory = new TopologyManagerFactoryDBImpl();

  private static final String defaultTopoManagerId = "123";

  public static TopologyManager getInstance() {
    return getDBInstance();
    //return getMemInstance();
  }

  public static TopologyManager getMemInstance() {
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

  public static TopologyManager getDBInstance() {
    try {
      if (dbfactory.hasTopologyManager(defaultTopoManagerId)) {
        TopologyManager manager = dbfactory.getTopologyManager(defaultTopoManagerId);
        try {
          if (!manager.containsKey("X"))
            manager.registerKey("X", "X coordinate", Double.class, DoubleConverter.class);
          if (!manager.containsKey("Y"))
            manager.registerKey("Y", "Y coordinate", Double.class, DoubleConverter.class);
        } catch (PropertyException e) {
          log.error("Error in registering property", e);
        }
        return manager;
      } else {
        TopologyManager manager = dbfactory.createTopologyManager(defaultTopoManagerId);
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
