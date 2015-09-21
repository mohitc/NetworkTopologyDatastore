package com.topology.primitives.properties.converters;

import com.topology.primitives.exception.properties.PropertyException;

//Interface to convert a property from base object to a string
public interface PropertyConverter<T> {

  public T fromString(String input) throws PropertyException;

  public String toString(T input) throws PropertyException;
}
