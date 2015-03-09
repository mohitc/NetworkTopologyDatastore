package com.topology.primitives.resource;

import java.util.Set;

import com.topology.primitives.exception.resource.ResourceException;

public interface ConnectionResource {
	
	public ConnectionResource join(ConnectionResource resource) throws ResourceException;
	
	public boolean canReserve(ConnectionResource resource) throws ResourceException;
	
	public ConnectionResource availableResource(ConnectionResource reservedResources) throws ResourceException;

	public ConnectionResource availableResource(Set<ConnectionResource> reservedResources) throws ResourceException;

}
