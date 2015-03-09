
package com.topology.primitives.connresource;

import com.topology.primitives.exception.resource.ResourceException;
import com.topology.primitives.resource.ConnectionResource;

import java.util.Set;

//Default connection resource that is used to only track path and not hinder resource usage in any way
public class DefaultConnectionResource implements ConnectionResource {
  @Override
  public ConnectionResource join(ConnectionResource resource) throws ResourceException {
    return this;
  }

  @Override
  public boolean canReserve(ConnectionResource resource) throws ResourceException {
    return true;
  }

  @Override
  public ConnectionResource availableResource(ConnectionResource reservedResources) throws ResourceException {
    return this;
  }

  @Override
  public ConnectionResource availableResource(Set<ConnectionResource> reservedResources) throws ResourceException {
    return this;
  }
}
