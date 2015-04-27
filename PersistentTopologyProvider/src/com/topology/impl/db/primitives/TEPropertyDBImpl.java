package com.topology.impl.db.primitives;

import com.topology.impl.db.primitives.converters.TEPropertyConverter;
import com.topology.primitives.properties.keys.TEPropertyKey;

import javax.persistence.*;

@Entity
@Table(name="te_properties")
public class TEPropertyDBImpl {

  @Id
  @Column(name="id")
  @GeneratedValue(strategy=GenerationType.AUTO)
  private int id;

  @Column(name="key")
  @Convert(converter= TEPropertyConverter.class)
  private TEPropertyKey key;

  @Column(name="value")
  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public TEPropertyKey getKey() {
    return key;
  }

  public void setKey(TEPropertyKey key) {
    this.key = key;
  }

  public boolean equals(Object o) {
    if ((o==null) || (!o.getClass().isAssignableFrom(TEPropertyDBImpl.class)))
      return false;
    if (((TEPropertyDBImpl)o).id == this.id)
      return true;
    return false;
  }

  public int hashCode() {
    return new Integer(this.id).hashCode();
  }
}
