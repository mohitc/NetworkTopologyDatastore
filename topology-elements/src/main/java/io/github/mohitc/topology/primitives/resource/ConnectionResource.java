package io.github.mohitc.topology.primitives.resource;

import io.github.mohitc.topology.primitives.exception.resource.ResourceException;

import java.util.Set;

//All implementations of the Connection Resource interface should implement the default constructor
public interface ConnectionResource {
	
	ConnectionResource join(ConnectionResource resource) throws ResourceException;
	
	boolean canReserve(ConnectionResource resource) throws ResourceException;
	
	ConnectionResource availableResource(ConnectionResource reservedResources) throws ResourceException;

	ConnectionResource availableResource(Set<ConnectionResource> reservedResources) throws ResourceException;

  String convertToString();

  ConnectionResource populateFromString(String in) throws ResourceException;

}
