package com.topology.primitives.resource;

import java.util.Set;

import com.topology.primitives.exception.resource.ResourceException;

//All implementations of the Connection Resource interface should implement the default constructor
public interface ConnectionResource {
	
	ConnectionResource join(ConnectionResource resource) throws ResourceException;
	
	boolean canReserve(ConnectionResource resource) throws ResourceException;
	
	ConnectionResource availableResource(ConnectionResource reservedResources) throws ResourceException;

	ConnectionResource availableResource(Set<ConnectionResource> reservedResources) throws ResourceException;

  String convertToString();

  ConnectionResource populateFromString(String in) throws ResourceException;

}
