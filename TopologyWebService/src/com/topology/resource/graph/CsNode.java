package com.topology.resource.graph;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Map;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class CsNode extends CsElement{

  private CsNodeData data;

  private Map<String, Object> scratch;

  private CsNodePosition position;

  private boolean selectable = true;

  private boolean locked = false;

  private boolean grabbable = true;

  private String classes;

  private Map<String, Object> style;

  public CsNode() {
    this.setGroup("nodes");
  }

  public CsNodeData getData() {
    return data;
  }

  public void setData(CsNodeData data) {
    this.data = data;
  }

  public Map<String, Object> getScratch() {
    return scratch;
  }

  public void setScratch(Map<String, Object> scratch) {
    this.scratch = scratch;
  }

  public CsNodePosition getPosition() {
    return position;
  }

  public void setPosition(CsNodePosition position) {
    this.position = position;
  }

  public boolean isSelectable() {
    return selectable;
  }

  public void setSelectable(boolean selectable) {
    this.selectable = selectable;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public boolean isGrabbable() {
    return grabbable;
  }

  public void setGrabbable(boolean grabbable) {
    this.grabbable = grabbable;
  }

  public String getClasses() {
    return classes;
  }

  public void setClasses(String classes) {
    this.classes = classes;
  }

  public Map<String, Object> getStyle() {
    return style;
  }

  public void setStyle(Map<String, Object> style) {
    this.style = style;
  }
}
