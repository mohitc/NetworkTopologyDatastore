package com.helpers.notification.manager.parser;

import com.helpers.notification.filters.NotificationFilter;
import com.helpers.notification.handlers.NotificationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

//Helper class to parse the configuration in the notfhandlers.xml.sample
public class NotificationHandlerParser {

  private static final Logger log = LoggerFactory.getLogger(NotificationHandlerParser.class);

  private static final String HANDLER_TAG = "handler";

  private static final String HANDLER_CLASS_TAG = "class";

  private static final String FILTER_LIST_TAG = "filters";

  private static final String FILTER_TAG = "filter";

  private static final String FILTER_CLASS_TAG = "class";

  private static final String FILTER_PARAM_TAG = "param";


  public List<NotificationProcessorConf> parse(URI confFile) {
    List<NotificationProcessorConf> confList = new ArrayList<>();
    try {
      if (confFile==null) {
        log.error("URI to configuration file is null");
        return confList;
      }
      File file = new File(confFile);
      if (!file.exists()) {
        log.error("Could not find or load notfhandlers.xml.sample");
        return confList;
      }
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document configDocument = docBuilder.parse(file);
      configDocument.getDocumentElement().normalize();
      NodeList handlerList = configDocument.getElementsByTagName(HANDLER_TAG);
      if ((handlerList == null) || (handlerList.getLength()<=0)) {
        log.info("No handlers found in the configuration file");
        return confList;
      }
      for (int i = 0; i < handlerList.getLength(); i++) {
        NotificationProcessorConf conf = parseHandlerNode(handlerList.item(i));
        if (conf!=null)
          confList.add(conf);
      }

    } catch (Exception e) {
      log.error("Could not find or load notfhandlers.xml.sample");
    }


    return confList;
  }


  private NotificationProcessorConf parseHandlerNode(Node handler) {
    if (handler.getNodeType() == Node.ELEMENT_NODE) {
      Element nodeHandler = (Element) handler;

      String className =null;
      NodeList classNameList = nodeHandler.getElementsByTagName(HANDLER_CLASS_TAG);
      if ((classNameList!=null)&&(classNameList.getLength()==1)) {
        className = classNameList.item(0).getTextContent();
      }

      if (className==null) {
        log.error("Class not defined inside handler");
        return null;
      }


      Class<? extends NotificationHandler> handlerInstance = getValidClass(className, NotificationHandler.class);

      if (handlerInstance==null) {
        return null;
      }
      // TODO handler class loaded. Parse parameters
      // For now default to empty constructor
      String handlerParams = null;


      NotificationHandler handlerObject = createObject(handlerInstance, null, NotificationHandler.class);
      if (handlerObject == null) {
        return null;
      }
      NotificationProcessorConf conf = new NotificationProcessorConf();
      conf.setHandler(handlerObject);
      //Generate Filter list
      List<NotificationFilter> filterList;
      NodeList temp  = nodeHandler.getElementsByTagName(FILTER_LIST_TAG);
      if ((temp==null) || (temp.getLength()!=1)) {
        log.error("Invalid filter configuration. Defaulting to empty list");
        filterList = new ArrayList<>();
      } else {
        filterList = parseFilterList(temp.item(0));
      }
      conf.setFilters(filterList);
      return conf;

    } else {
      log.error("Invalid XML format for handler");
      return null;
    }

  }

  private List<NotificationFilter> parseFilterList(Node filters) {
    List<NotificationFilter> filterlist = new ArrayList<>();
    if (filters==null) {
      log.info("No filters defined for Handler");
      return filterlist;
    }
    if (filters.getNodeType() == Node.ELEMENT_NODE) {
      NodeList filterList = ((Element) filters).getElementsByTagName(FILTER_TAG);
      if (filterlist==null) {
        log.info("No filters defined for Handler");
        return filterlist;
      }
      for (int i=0;i<filterList.getLength(); i++) {
        NotificationFilter filter = parseFilter(filterList.item(i));
        if (filter!=null) {
          filterlist.add(filter);
        }
      }
    }
    return filterlist;
  }

  private NotificationFilter parseFilter(Node filter) {
    if ((filter==null) || (filter.getNodeType()!=Node.ELEMENT_NODE)) {
      log.error("Invalid filter defined");
      return null;
    }
    Element filterElement = (Element) filter;

    String className = filterElement.getAttribute(FILTER_CLASS_TAG);
    if (className==null) {
      log.error("Class attribute not defined");
      return null;
    }

    Class<? extends NotificationFilter> filterClass = getValidClass(className, NotificationFilter.class);

    if (filterClass==null) {
      return null;
    }

    //Filter class is not null, parse parameter tags and create a corresponding string array
    NodeList paramList = filterElement.getElementsByTagName(FILTER_PARAM_TAG);
    String[] params = null;
    if ((paramList!=null) && (paramList.getLength()>0)) {
      //one or more parameters defined
      params = new String[paramList.getLength()];
      for (int i=0;i<paramList.getLength();i++) {
        params[i] = paramList.item(i).getTextContent();
      }
    }

    return createObject(filterClass, params, NotificationFilter.class);
  }


  private <K> K createObject(Class<? extends K> objectClass, String[] params, Class<K> instance) {
    try {
      if (params==null) {
        Constructor<? extends K> handlerConstructor = objectClass.getConstructor(null);
        return handlerConstructor.newInstance();
      } else {
        Constructor<? extends K> handlerConstructor = objectClass.getConstructor(params.getClass());
        return handlerConstructor.newInstance((Object)params);
      }
    } catch (NoSuchMethodException e) {
      log.error("Default constructor not defined for class: " + objectClass.getSimpleName(), e);
    } catch (Exception e) {
      log.error("Error instantiating " + objectClass.getSimpleName(), e);
    }
    return null;
  }

  private <K> Class<? extends K> getValidClass(String className, Class<K> baseType) {
    try {
      Class <? extends K> handlerInstance = null;
      Class _instance = Class.forName(className);
      if (baseType.isAssignableFrom(_instance)) {
        return (Class<? extends K>)_instance;
      } else {
        log.error("Class " + className + " not a subclass of " + baseType.getSimpleName());
      }
    } catch (ClassNotFoundException e) {
      log.error("Handler class " + className + " not found", e);
    }
    return null;
  }

}
