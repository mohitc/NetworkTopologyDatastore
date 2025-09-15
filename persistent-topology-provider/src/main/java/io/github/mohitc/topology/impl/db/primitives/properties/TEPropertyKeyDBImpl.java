package io.github.mohitc.topology.impl.db.primitives.properties;

import io.github.mohitc.topology.impl.db.primitives.converters.ConverterClassConverter;
import io.github.mohitc.topology.impl.db.primitives.converters.ObjectClassConverter;
import io.github.mohitc.topology.primitives.exception.properties.PropertyException;
import io.github.mohitc.topology.primitives.properties.TEPropertyKey;
import io.github.mohitc.topology.primitives.properties.converters.PropertyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.*;
import java.lang.reflect.InvocationTargetException;

@Entity
@Table(name="te_property_keys")
@NamedQueries({
    @NamedQuery(name = TEPropertyKeyDBImpl.GET_ALL_KEYS_BY_MANAGER, query = "Select t from TEPropertyKeyDBImpl t WHERE (t.manager = :manager) "),
    @NamedQuery(name = TEPropertyKeyDBImpl.GET_KEY_BY_MANAGER_AND_ID, query = "Select t from TEPropertyKeyDBImpl t WHERE (t.manager = :manager AND t.keyID = :keyID) ")
})
public class TEPropertyKeyDBImpl implements TEPropertyKey {

  public static final String GET_ALL_KEYS_BY_MANAGER = "GET_ALL_KEYS_BY_MANAGER";
  public static final String GET_KEY_BY_MANAGER_AND_ID = "GET_KEY_BY_MANAGER_AND_ID";

  private static final Logger log = LoggerFactory.getLogger(TEPropertyKeyDBImpl.class);

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy= GenerationType.AUTO)
  private int id;


  @Column(name = "manager", nullable = false)
  private String manager;

  @Column(name = "keyid", nullable = false)
  private String keyID;

  @Column(name = "descr")
  private String description;

  @Column(name="objclass")
  @Convert(converter= ObjectClassConverter.class)
  private Class objClass;

  @Column(name="convclass")
  @Convert(converter= ConverterClassConverter.class)
  private Class<? extends PropertyConverter> converterClass;

  public TEPropertyKeyDBImpl() {
    // Empty constructor required for serialization
  }

  public TEPropertyKeyDBImpl(String manager, String keyID, String description, Class objClass, Class<? extends PropertyConverter> converterClass) {
    this.manager = manager;
    this.keyID = keyID;
    this.description = description;
    this.objClass = objClass;
    this.converterClass = converterClass;
  }

  @Override
  public String id() {
    return keyID;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public Class objClass() {
    return objClass;
  }

  @Override
  public Class<? extends PropertyConverter> converterClass() {
    return converterClass;
  }

  @Override
  public PropertyConverter getConverter() throws PropertyException {
    if (converterClass==null) {
      throw new PropertyException("Converter class not defined");
    }
    try {
      return converterClass.getConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      log.error("Error while creating property converter instance", e);
      throw new PropertyException("Error while creating property converter instance: " + e.getMessage(), e);
    }
  }

  @Override
  public boolean equals (Object o) {
    if (o!=null) {
      if (o instanceof TEPropertyKey key) {
        return key.id().equals(this.id()) && key.objClass().equals(this.objClass());
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    return keyID.hashCode() * 31 * 31 + objClass.hashCode() * 31 + manager.hashCode();
  }
}
