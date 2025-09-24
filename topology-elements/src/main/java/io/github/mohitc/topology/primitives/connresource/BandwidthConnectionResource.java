package io.github.mohitc.topology.primitives.connresource;

import io.github.mohitc.topology.primitives.exception.resource.ResourceException;
import io.github.mohitc.topology.primitives.resource.ConnectionResource;

import java.util.Set;

public class BandwidthConnectionResource implements ConnectionResource {

  private double bandwidth;

  public BandwidthConnectionResource(double bandwidth) {
    this.bandwidth = bandwidth;
  }

  public BandwidthConnectionResource() {
    // Empty constructor to support serialization
  }

  /**
   * Checks if the resource is not null and is of type bandwidth connection resource
   *
   * @param resource
   * @throws ResourceException
   */
  private void checkResourceType(ConnectionResource resource) throws ResourceException {
    if (resource == null) {
      throw new ResourceException("Resource provided for join cannot be null");
    }
    if (!BandwidthConnectionResource.class.isAssignableFrom(resource.getClass())) {
      throw new ResourceException("Incompatible resource types attempted during join operation");
    }
  }

  @Override
  public ConnectionResource join(ConnectionResource resource) throws ResourceException {
    //Checks if resource types are compatible
    checkResourceType(resource);
    return new BandwidthConnectionResource(this.bandwidth + ((BandwidthConnectionResource) resource).getBandwidth());
  }

  @Override
  public boolean canReserve(ConnectionResource resource) throws ResourceException {
    //Checks if resource types are compatible
    checkResourceType(resource);
    return ((BandwidthConnectionResource) resource).getBandwidth() < this.getBandwidth();
  }

  @Override
  public ConnectionResource availableResource(ConnectionResource reservedResources) throws ResourceException {
    //Checks if resource types are compatible
    checkResourceType(reservedResources);
    if (canReserve(reservedResources)) {
      return new BandwidthConnectionResource(this.bandwidth - ((BandwidthConnectionResource) reservedResources).getBandwidth());
    } else {
      throw new ResourceException("Reserved resources are greater than the total available resources on the connection");
    }
  }

  @Override
  public ConnectionResource availableResource(Set<ConnectionResource> reservedResources) throws ResourceException {
    if (reservedResources == null || reservedResources.isEmpty()) {
      return this;
    }
    ConnectionResource availableResource = null;
    for (ConnectionResource resource : reservedResources) {
      if (availableResource == null) {
        availableResource = this.availableResource(resource);
      } else {
        availableResource = availableResource.availableResource(resource);
      }
    }
    return availableResource;
  }

  @Override
  public String convertToString() {
    return Double.toString(bandwidth);
  }

  @Override
  public ConnectionResource populateFromString(String in) throws ResourceException {
    if (in == null) {
      throw new ResourceException("Input String cannot be null");
    }
    try {
      double bandwidth = Double.parseDouble(in);
      return new BandwidthConnectionResource(bandwidth);
    } catch (NumberFormatException e) {
      throw new ResourceException("Could not convert string {" + in + "} to a Bandwidth connection resource", e);
    }
  }

  /**
   * Function to get the bandwidth available with the connection resource
   *
   * @return
   */
  public double getBandwidth() {
    return bandwidth;
  }
}
