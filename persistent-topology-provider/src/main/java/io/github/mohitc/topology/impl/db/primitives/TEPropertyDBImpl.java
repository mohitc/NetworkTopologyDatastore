package io.github.mohitc.topology.impl.db.primitives;

import io.github.mohitc.topology.impl.db.primitives.properties.TEPropertyKeyDBImpl;
import io.github.mohitc.topology.primitives.properties.TEPropertyKey;

import jakarta.persistence.*;

@Entity
@Table(name="te_properties")
public class TEPropertyDBImpl {

  @Id
  @Column(name="id")
  @GeneratedValue(strategy=GenerationType.AUTO)
  private int id;


  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="key_id", referencedColumnName="id")
  private TEPropertyKeyDBImpl key;

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
    if (key instanceof TEPropertyKeyDBImpl) {
      this.key = (TEPropertyKeyDBImpl)key;
    }
  }

  @Override
  public boolean equals(Object o) {
    if ((o==null) || (!o.getClass().isAssignableFrom(TEPropertyDBImpl.class))) {
      return false;
    }
    return ((TEPropertyDBImpl)o).id == this.id;
  }

  @Override
  public int hashCode() {
    return Integer.valueOf(this.id).hashCode();
  }
}
