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

public class SNDLibImportTopology implements ImportTopology {

  private static final Logger log = LoggerFactory.getLogger(SNDLibImportTopology.class);

  private double trafficScalingFactor = 1.0;
  private double linkScalingFactor = 1.0;

  //Boolean to indicate if duplicate links between the same node pair should be removed.
  private final boolean removeDuplicates;

  public SNDLibImportTopology() {
    removeDuplicates=true;
  }

  public SNDLibImportTopology(boolean removeDuplicates) {
    this.removeDuplicates = removeDuplicates;
  }

  private void createNodes(Document doc, TopologyManager manager) throws FileFormatException, TopologyException {
    NodeList list = doc.getElementsByTagName("nodes");
    if (list.getLength()!=1) {
      throw new FileFormatException("The document should only have one tag with the list of all network elements");
    }

    TEPropertyKey XCOORD = manager.getKey("X");
    TEPropertyKey YCOORD = manager.getKey("Y");

    //load scaling factor from file
    File scaleFile = new File("conf/scaling.xml");
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;
    Document scaleDoc = null;
    try {
      docBuilder = docFactory.newDocumentBuilder();
      doc = docBuilder.parse(scaleFile);
      if (doc==null) {
        log.error("No scaling document found. Please check XML file. Defaulting to 1.0");
      }
      //Normalize document
      doc.normalizeDocument();
      NodeList l = doc.getElementsByTagName("root");
      linkScalingFactor = Double.parseDouble(l.item(0).getChildNodes().item(1).getTextContent());
      log.info("Link scaling factor set to "+Double.toString(trafficScalingFactor));
    } catch (Exception e) {
      log.error("Error while setting up link scaling factor, defaulting to 1.0");
      log.debug("Exception caught while setting up link scaling factor", e);
      linkScalingFactor = 1.0;
    }

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
        log.debug("Generating new network element. Label: {}", node.getLabel());

        //Populate coordinates
        NodeList coordTagList = neVals.getElementsByTagName("coordinates");
        if ((coordTagList!=null) && (coordTagList.getLength()>0)) {
          Node coords = coordTagList.item(0);
          if (coords.getNodeType()== Node.ELEMENT_NODE) {
            node.addProperty(XCOORD, Double.parseDouble(((Element)coords).getElementsByTagName("x").item(0).getTextContent()));
            node.addProperty(YCOORD, Double.parseDouble(((Element) coords).getElementsByTagName("y").item(0).getTextContent()));
            log.debug("Coordinates for node: {}, (X, Y): ({}, {})", node.getLabel(), node.getProperty(XCOORD), node.getProperty(YCOORD));
          }
        }

        //generate a single port in the network element
        ConnectionPoint cp = manager.createPort(node);
        cp.setLabel(node.getLabel());
        log.debug("Port Created: {}", cp.getLabel());
      }
    }

  }

  //haversine formula for computing distance from latitude and longitude
  public final static double AVERAGE_RADIUS_OF_EARTH = 6373; //Km
  public int sphericalDistanceFromLatLon(double lat1, double lon1,
                                         double lat2, double lon2) {

    double latDistance = Math.toRadians(lat1 - lat2);
    double lngDistance = Math.toRadians(lon1 - lon2);

    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));
  }

  private void createLinks(Document doc, TopologyManager manager) throws FileFormatException, TopologyException {
    NodeList list = doc.getElementsByTagName("links");
    if (list.getLength()!=1) {
      throw new FileFormatException("The document should only have one tag with the list of all network elements");
    }

    //load scaling factor from file
    File scaleFile = new File("conf/scaling.xml");
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;
    Document scaleDoc = null;
    try {
      docBuilder = docFactory.newDocumentBuilder();
      doc = docBuilder.parse(scaleFile);
      if (doc==null) {
        log.error("No scaling document found. Please check XML file. Defaulting to 1.0");
      }
      //Normalize document
      doc.normalizeDocument();
      NodeList l = doc.getElementsByTagName("root");
      trafficScalingFactor = Double.parseDouble(l.item(0).getChildNodes().item(0).getTextContent());
      log.info("Traffic scaling factor set to "+Double.toString(trafficScalingFactor));
    } catch (Exception e) {
      log.error("Error while setting up traffic scaling factor, defaulting to 1.0");
      log.debug("Exception caught while setting up link scaling factor", e);
      trafficScalingFactor = 1.0;
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

        if (removeDuplicates) {
          if (aEnd.getConnections().stream()
              .anyMatch(v -> v.getaEnd().getID() == zEnd.getID() || v.getzEnd().getID() == zEnd.getID())) {
            log.info("Ignoring duplicate connection {} between {} and {}", label, aEnd, zEnd);
            continue;
          }
        }

        Link link = manager.createLink(aEnd.getID(), zEnd.getID());
        link.setLabel(label);
        link.setDirected(false);
        link.setLayer(NetworkLayer.PHYSICAL);
        log.debug("New link created from {} to {}", aEnd.getLabel(), zEnd.getLabel());

        //@Fed3: compute link length and delay
        TEPropertyKey XCOORD = manager.getKey("X");
        TEPropertyKey YCOORD = manager.getKey("Y");
        TEPropertyKey Delay = manager.getKey("Delay");
        TEPropertyKey Capacity = manager.getKey("Capacity");
//        double x1 = (Double)aEnd.getParent().getProperty(XCOORD);
//        double x2 = (Double)zEnd.getParent().getProperty(XCOORD);
//        double y1 = (Double)aEnd.getParent().getProperty(YCOORD);
//        double y2 = (Double)zEnd.getParent().getProperty(YCOORD);
//        double distance = Math.sqrt( Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0) );
        double lat1 = (Double)aEnd.getParent().getProperty(XCOORD);
        double lat2 = (Double)zEnd.getParent().getProperty(XCOORD);
        double lon1 = (Double)aEnd.getParent().getProperty(YCOORD);
        double lon2 = (Double)zEnd.getParent().getProperty(YCOORD);
        double distance = sphericalDistanceFromLatLon(lat1, lon1, lat2, lon2) * linkScalingFactor;
        log.debug("Link is {} Km long", distance);
        double delay = distance / 200000; //T = S/V, V = 2/3 C ~= 200000 Km/s
        link.addProperty(Delay, delay);
        log.debug("Link has delay {}", delay);
        double capacity = 0; //TODO: read from file once known
        link.addProperty(Capacity, capacity);
        log.debug("Link has capacity {}", capacity);

      }
    }
  }

  private void createDemands(Document doc, TopologyManager manager) throws FileFormatException, TopologyException {
    NodeList list = doc.getElementsByTagName("demands");
    if (list.getLength()!=1) {
      throw new FileFormatException("The document should only have one tag with the list of all demands");
    }
    //create demands store in the manager
    TEPropertyKey demandStoreKey = manager.getKey("Demands");
    Map<String, Double> demandStore = new HashMap<>();

    //load scaling factor from file
    File scaleFile = new File("conf/scaling.xml");
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;
    Document scaleDoc = null;
    try {
      docBuilder = docFactory.newDocumentBuilder();
      doc = docBuilder.parse(scaleFile);
      if (doc==null) {
        log.error("No scaling document found. Please check XML file. Defaulting to 1.0");
      }
      //Normalize document
      doc.normalizeDocument();
      NodeList l = doc.getElementsByTagName("root");
      trafficScalingFactor = Double.parseDouble(l.item(0).getChildNodes().item(0).getTextContent());
      log.info("Scaling factor set to "+Double.toString(trafficScalingFactor));
    } catch (Exception e) {
      log.debug("Error while setting up scaling factor, defaulting to 1.0", e);
      trafficScalingFactor = 1.0;
    }

    //create demands
    NodeList demandsList = list.item(0).getChildNodes();
    for (int i=0;i<demandsList.getLength(); i++) {
      Node demandDesc = demandsList.item(i);
      if (demandDesc.getNodeType() == Node.ELEMENT_NODE) {
        Element demandVals = (Element) demandDesc;
        String aEnd = demandVals.getElementsByTagName("source").item(0).getTextContent();
        String zEnd = demandVals.getElementsByTagName("target").item(0).getTextContent();
        String label = "{" + aEnd + "}{" + zEnd +"}";
        double d = Double.parseDouble(demandVals.getElementsByTagName("demandValue").item(0).getTextContent());
        String capacity = Double.toString(d * trafficScalingFactor);
        demandStore.put(label, Double.parseDouble(capacity));
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
