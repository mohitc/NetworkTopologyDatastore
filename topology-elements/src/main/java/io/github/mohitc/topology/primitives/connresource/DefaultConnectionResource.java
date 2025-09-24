
package io.github.mohitc.topology.primitives.connresource;

import io.github.mohitc.topology.primitives.exception.resource.ResourceException;
import io.github.mohitc.topology.primitives.resource.ConnectionResource;

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

  @Override
  public String convertToString() {
    return "";
  }

  @Override
  public ConnectionResource populateFromString(String in) throws ResourceException {
    return new DefaultConnectionResource();
  }
}
