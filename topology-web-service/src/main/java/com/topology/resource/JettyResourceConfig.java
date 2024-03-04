package com.topology.resource;


import com.topology.impl.importers.sndlib.SNDLibImportTopology;
import com.topology.importers.ImportTopology;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.FileFormatException;
import com.topology.primitives.exception.TopologyException;
import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.impl.DoubleConverter;
import com.topology.resource.conn.ConnectionResource;
import com.topology.resource.cp.ConnectionPointResource;
import com.topology.resource.exception.TopologyExceptionMapper;
import com.topology.resource.graph.CsGraphResource;
import com.topology.resource.manager.TopologyManagerFactoryHelper;
import com.topology.resource.ne.NetworkElementResource;
import com.topology.resource.te.TopologyElementResource;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;

@ApplicationPath("/")
class JettyResourceConfig extends ResourceConfig {

  private static final Logger log = LoggerFactory.getLogger(JettyResourceConfig.class);

  public JettyResourceConfig() {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
    register(new ConnectionResource());
    register(new ConnectionPointResource());
    register(new TopologyExceptionMapper());
    register(new CsGraphResource());
    register(new NetworkElementResource());
    register(new TopologyElementResource());
    try {
      TopologyManagerFactoryHelper.getInstance().createTopologyManager("test");
      ImportTopology importer = new SNDLibImportTopology();
      TopologyManager manager = TopologyManagerFactoryHelper.getInstance().getTopologyManager("test");
      try {
        if (!manager.containsKey("X"))
          manager.registerKey("X", "X coordinate", Double.class, DoubleConverter.class);
        if (!manager.containsKey("Y"))
          manager.registerKey("Y", "Y coordinate", Double.class, DoubleConverter.class);
      } catch (PropertyException e) {
        log.error("Error in registering property", e);
      }
      String fileName = this.getClass().getClassLoader().getResource("/").getFile() + "//abilene.xml";
      importer.importFromFile(fileName, manager);
    } catch (TopologyException e) {
      log.error("Topology Exception while parsing test.topology file", e);
    } catch (FileFormatException e) {
      log.error("FileFormat Exception while parsing test.topology file", e);
    } catch (IOException e) {
      log.error("IO Exception while parsing test.topology file", e);
    }

  }
}