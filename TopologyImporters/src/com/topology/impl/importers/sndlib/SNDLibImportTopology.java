package com.topology.impl.importers.sndlib;

import com.topology.importers.ImportTopology;
import com.topology.primitives.*;
import com.topology.primitives.exception.FileFormatException;
import com.topology.primitives.exception.TopologyException;
import com.topology.primitives.exception.properties.PropertyException;
import com.topology.primitives.properties.TEPropertyKey;

import com.topology.primitives.properties.converters.impl.DoubleConverter;
import com.topology.primitives.properties.converters.impl.MapConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SNDLibImportTopology implements ImportTopology {

  private static final Logger log = LoggerFactory.getLogger(SNDLibImportTopology.class);

  private void createNodes(Document doc, TopologyManager manager) throws FileFormatException, TopologyException {
    NodeList list = doc.getElementsByTagName("nodes");
    if (list.getLength()!=1) {
      throw new FileFormatException("The document should only have one tag with the list of all network elements");
    }

    TEPropertyKey XCOORD = manager.getKey("X");
    TEPropertyKey YCOORD = manager.getKey("Y");

    //create nodes
    NodeList nesList = list.item(0).getChildNodes();
    for (int i=0;i<nesList.getLength(); i++) {
      Node ne = nesList.item(i);
      if (ne.getNodeType() == Node.ELEMENT_NODE) {
        //Create the network element
        Element neVals = (Element) ne;
        String label = neVals.getAttribute("id");
        NetworkElement node = manager.createNetworkElement();
        node.setLabel(label);
        log.info("Generating new network element. Label: " + node.getLabel());

        //Populate coordinates
        NodeList coordTagList = neVals.getElementsByTagName("coordinates");
        if ((coordTagList!=null) && (coordTagList.getLength()>0)) {
          Node coords = coordTagList.item(0);
          if (coords.getNodeType()== Node.ELEMENT_NODE) {
            node.addProperty(XCOORD, Double.parseDouble(((Element)coords).getElementsByTagName("x").item(0).getTextContent()));
            node.addProperty(YCOORD, Double.parseDouble(((Element) coords).getElementsByTagName("y").item(0).getTextContent()));
            log.info("Coordinates for node: " + node.getLabel() + ", (X, Y): (" + node.getProperty(XCOORD) + ", " + node.getProperty(YCOORD) + ")");
          }
        }

        //generate a single port in the network element
        ConnectionPoint cp = manager.createPort(node);
        cp.setLabel(node.getLabel());
        log.info("Port Created: " + cp.getLabel());
      }
    }

  }

  private void createLinks(Document doc, TopologyManager manager) throws FileFormatException, TopologyException {
    NodeList list = doc.getElementsByTagName("links");
    if (list.getLength()!=1) {
      throw new FileFormatException("The document should only have one tag with the list of all network elements");
    }

    //create nodes
    NodeList linksList = list.item(0).getChildNodes();
    for (int i=0;i<linksList.getLength(); i++) {
      Node linkDesc = linksList.item(i);
      if (linkDesc.getNodeType() == Node.ELEMENT_NODE) {
        Element linkVals = (Element) linkDesc;
        String label = linkVals.getAttribute("id");

        //Get link endpoints
        String aEndLabel = linkVals.getElementsByTagName("source").item(0).getTextContent();
        String zEndLabel = linkVals.getElementsByTagName("target").item(0).getTextContent();
        Port aEnd = manager.getSingleElementByLabel(aEndLabel, Port.class);
        Port zEnd = manager.getSingleElementByLabel(zEndLabel, Port.class);

        Link link = manager.createLink(aEnd.getID(), zEnd.getID());
        link.setLabel(label);
        link.setDirected(false);
        log.info("New link created from " + aEnd.getLabel() + " to " + zEnd.getLabel());

        //@Fed3: compute link length and delay
        TEPropertyKey XCOORD = manager.getKey("X");
        TEPropertyKey YCOORD = manager.getKey("Y");
        TEPropertyKey Delay = manager.getKey("Delay"); //TODO: need to register this somewhere
        TEPropertyKey Capacity = manager.getKey("Capacity");//TODO: need to register this somewhere
        double x1 = (Double)aEnd.getParent().getProperty(XCOORD);
        double x2 = (Double)zEnd.getParent().getProperty(XCOORD);
        double y1 = (Double)aEnd.getParent().getProperty(YCOORD);
        double y2 = (Double)zEnd.getParent().getProperty(YCOORD);
        double distance = Math.sqrt( Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0) );
        double CFactor = 1.0; //TODO: set correct value, find more elegant solution
        double delay = distance * CFactor / 200000;
        link.addProperty(Delay, delay);
        log.info("Link has delay " + Double.toString(delay));
        double capacity = 0; //TODO: read from file once known
        link.addProperty(Capacity, capacity);
        log.info("Link has capacity " + Double.toString(capacity));

      }
    }
  }

  private void createDemands(Document doc, TopologyManager manager) throws FileFormatException, TopologyException {
    NodeList list = doc.getElementsByTagName("demands");
    if (list.getLength()!=1) {
      throw new FileFormatException("The document should only have one tag with the list of all demands");
    }

    //create demands store in the manager
    TEPropertyKey demandStoreKey = manager.getKey("Demands");//TODO: need to register this somewhere
    Map<String, Map<String, String> > demandStore = new HashMap<>();

    //create nodes
    NodeList demandsList = list.item(0).getChildNodes();
    for (int i=0;i<demandsList.getLength(); i++) {
      Node demandDesc = demandsList.item(i);
      if (demandDesc.getNodeType() == Node.ELEMENT_NODE) {
        Element demandVals = (Element) demandDesc;
        String label = demandVals.getAttribute("id");
        String aEnd = demandVals.getAttribute("source");
        String zEnd = demandVals.getAttribute("target");
//	    	  Double capacity = Double.valueOf(demandVals.getAttribute("demandValue"));
        String capacity = demandVals.getAttribute("demandValue");

        Map<String, String> demand = new HashMap<>();
        demand.put("id", label);
        demand.put("aEnd", aEnd);
        demand.put("zEnd", zEnd);
        demand.put("capacity", capacity);

        demandStore.put(label, demand);
      }
    }
    manager.addProperty(demandStoreKey, demandStore);
  }

  @Override
  public void importFromFile(String fileName, TopologyManager manager) throws TopologyException, FileFormatException, IOException {
    log.info("Starting scan of test.topology based on the SNDLib XML test.topology format");
    File topoFile = new File(fileName);
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    Document doc = null;
    try {
      builder = factory.newDocumentBuilder();
      doc = builder.parse(topoFile);
    } catch (ParserConfigurationException e) {
      log.error("Error while setting up XML parser", e);
      throw new FileFormatException(e.getMessage());
    } catch (SAXException e) {
      log.error("File Parsing Exception", e);
      throw new FileFormatException(e.getMessage());
    }
    //Document should not be null
    if (doc!=null) {
      //Start scan of elements
      doc.getDocumentElement().normalize();
      createKeys(manager);
      createNodes(doc, manager);
      createLinks(doc, manager);
      createDemands(doc, manager);
    } else {
      log.error("No document found");
    }
  }

  private void createKeys(TopologyManager manager) {
    if (manager == null){
      log.error("Createkeys called without valid topology manager");
    }
    try{
      manager.registerKey("X", "X coordinate", Double.class, DoubleConverter.class);
      manager.registerKey("Y", "Y coordinate", Double.class, DoubleConverter.class);
      manager.registerKey("Delay", "Delay (ms)", Double.class, DoubleConverter.class);
      manager.registerKey("Capacity", "Capacity (Gbps)", Double.class, DoubleConverter.class);
      manager.registerKey("Demands", "Demands for the topology", Map.class, MapConverter.class);
    } catch (PropertyException e) {
      log.error("problem registering keys");
    }

  }
}
