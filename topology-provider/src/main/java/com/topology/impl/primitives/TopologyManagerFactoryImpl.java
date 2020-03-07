package com.topology.impl.primitives;

import com.topology.primitives.TopologyManager;
import com.topology.primitives.TopologyManagerFactory;
import com.topology.primitives.exception.TopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TopologyManagerFactoryImpl implements TopologyManagerFactory {

  private Map<String, TopologyManager> managerMap = new HashMap<>();

  private static Logger log = LoggerFactory.getLogger(TopologyManagerFactoryImpl.class);

  @Override
  public TopologyManager createTopologyManager(String id) throws TopologyException {
    if (id==null)
      throw new TopologyException("ID cannot be null");
    id = id.toLowerCase().trim();
    if (managerMap.containsKey(id))
      throw new TopologyException("ID is not unique, a topology manager instance with ID: "+ id + " exists");
    TopologyManager instance = new TopologyManagerImpl(id);
    managerMap.put(id, instance);
    return instance;
  }

  @Override
  public TopologyManager getTopologyManager(String id) throws TopologyException {
    if (id==null)
      throw new TopologyException("ID cannot be null");
    if (managerMap.containsKey(id)) {
      return managerMap.get(id);
    } else
      throw new TopologyException("Topology manager with ID : " + id + " does not exist");
  }

  @Override
  public boolean hasTopologyManager(String id) {
    if (id==null) {
      log.error("ID cannot be null");
      return false;
    }
    return managerMap.containsKey(id);
  }

  @Override
  public void removeTopologyManager(String id) throws TopologyException {
    if (id==null)
      throw new TopologyException("ID cannot be null");
    if (managerMap.containsKey(id)) {
      log.info("Removing topology manager with ID: " + id);
      managerMap.remove(id);
    } else
      throw new TopologyException("Topology manager with ID : " + id + " does not exist");
  }
}
