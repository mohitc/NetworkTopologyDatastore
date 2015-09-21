package com.topology.ws.launcher;

import java.io.IOException;
import java.net.MalformedURLException;

import com.topology.impl.importers.sndlib.SNDLibImportTopology;
import com.topology.importers.ImportTopology;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.FileFormatException;
import com.topology.primitives.exception.TopologyException;
import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.converters.impl.DoubleConverter;
import com.topology.resource.manager.TopologyManagerFactoryHelper;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class WsLauncher {

	private static Server server;

	private static int port=8080;
	
	private static Logger log = LoggerFactory.getLogger(WsLauncher.class);

	/**
	 * Function to generate the Jersey handler, with configurations for resource scanning and filter inclusion
	 *
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	private static ServletContextHandler getJerseyContext()
			throws IOException {
		// Code to add Jersey Servlet to the Jetty Handlers
		log.info("Initializing Jersey Servlet configuration");
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		ServletHolder h = new ServletHolder(new ServletContainer());
		h.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
		h.setInitParameter("com.sun.jersey.config.property.packages",
				"com.topology.resource");
		h.setInitOrder(1);
		context.addServlet(h, "/rest/*");
		return context;
	}




	/**
	 * Function to generate the ResourceHandler to serve static HTML/CSS/JS
	 * content
	 *
	 * @return
	 */
	private static ContextHandler getStaticContext()
			throws MalformedURLException, IOException {
		// Code to add Jersey Servlet to the Jetty Handlers
		log.info("Initializing Static Resource Handler configuration");
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setBaseResource(Resource
				.newResource("ws/DocumentRoot"));
		ContextHandler handler = new ContextHandler();
		handler.setHandler(resource_handler);
		handler.setContextPath("/");
		return handler;
	}

	public static void main (String[] args) {

		try {

			// Jersey uses java.util.logging - bridge to slf4
			SLF4JBridgeHandler.removeHandlersForRootLogger();
			SLF4JBridgeHandler.install();

			log.info("Initializing Jetty Server");
			server = new Server(port);

			log.info("Adding Handlers and starting Jetty Server");
			HandlerList handlers = new HandlerList();
			handlers.setHandlers(new Handler[] { getJerseyContext(),
					getStaticContext(), new DefaultHandler() });

			server.setHandler(handlers);
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
      try {
        importer.importFromFile(".//resources//import//sndlib//abilene.xml", manager);
      } catch (TopologyException e) {
        log.error("Topology Exception while parsing test.topology file", e);
      } catch (FileFormatException e) {
        log.error("FileFormat Exception while parsing test.topology file", e);
      } catch (IOException e) {
        log.error("IO Exception while parsing test.topology file", e);
      }
      server.start();
			server.join();
			log.info("Jetty Server Started Successfully");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}

