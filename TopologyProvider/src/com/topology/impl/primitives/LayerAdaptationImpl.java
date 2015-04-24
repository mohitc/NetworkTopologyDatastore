package com.topology.impl.primitives;

import com.topology.primitives.Service;
import com.topology.primitives.TopologyElement;

import java.util.Collections;
import java.util.List;

//Layer adaptation is a very generic adaptation representation to support mapping of any type service to any type of topology element resources in the client layer
public class LayerAdaptationImpl {

  private List<TopologyElement> clientEntities;

  private List<Service> serverEntities;

  public LayerAdaptationImpl(List<TopologyElement> clientEntities, List<Service> serverEntities) {
    if (clientEntities!=null) {
      this.clientEntities = Collections.unmodifiableList(clientEntities);
    } else {
      this.clientEntities = Collections.EMPTY_LIST;
    }

    if (serverEntities!=null) {
      this.serverEntities = Collections.unmodifiableList(serverEntities);
    } else {
      serverEntities = Collections.EMPTY_LIST;
    }
  }

  public List<TopologyElement> getExposedClientEntities(){
    return clientEntities;
  }

  public List<Service> getUtilizedServices() {
    return serverEntities;
  }
}
