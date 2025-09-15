package io.github.mohitc.topology.impl.primitives;

import io.github.mohitc.topology.primitives.Service;
import io.github.mohitc.topology.primitives.TopologyElement;

import java.util.Collections;
import java.util.List;

//Layer adaptation is a very generic adaptation representation to support mapping of any type service to any type of topology element resources in the client layer
public class LayerAdaptationImpl {

  private final List<TopologyElement> clientEntities;

  private final List<Service> serverEntities;

  public LayerAdaptationImpl(List<TopologyElement> clientEntities, List<Service> serverEntities) {
    if (clientEntities!=null) {
      this.clientEntities = Collections.unmodifiableList(clientEntities);
    } else {
      this.clientEntities = Collections.emptyList();
    }

    if (serverEntities!=null) {
      this.serverEntities = Collections.unmodifiableList(serverEntities);
    } else {
      this.serverEntities = Collections.emptyList();
    }
  }

  public List<TopologyElement> getExposedClientEntities(){
    return clientEntities;
  }

  public List<Service> getUtilizedServices() {
    return serverEntities;
  }
}
