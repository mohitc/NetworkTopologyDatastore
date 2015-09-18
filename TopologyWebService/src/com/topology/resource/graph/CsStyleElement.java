package com.topology.resource.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CsStyleElement {

  private static final Logger log = LoggerFactory.getLogger(CsStyleElement.class);

  private String selector;

  private Map<String, Object> css = new HashMap<>();

  public String getSelector() {
    return selector;
  }

  public void setSelector(String selector) {
    this.selector = selector;
  }

  public Map<String, Object> getCss() {
    return css;
  }

  public void setCss(Map<String, Object> css) {
    this.css = css;
  }

  public void addCssProperty(String key, Object value) {
    if ((key==null) || (value == null))
      log.error("Invalid Key value pair provided for css style");
    css.put(key, value);
  }
}
