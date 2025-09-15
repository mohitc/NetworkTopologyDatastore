package io.github.mohitc.topology.primitives.properties.converters;

import io.github.mohitc.topology.primitives.exception.properties.PropertyException;

//Interface to convert a property from base object to a string
public interface PropertyConverter<T> {

  T fromString(String input) throws PropertyException;

  String toString(T input) throws PropertyException;
}
