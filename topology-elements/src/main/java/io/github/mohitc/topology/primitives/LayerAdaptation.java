package io.github.mohitc.topology.primitives;

import java.util.List;

//Layer adaptation is a very generic adaptation representation to support mapping of any type service to any type of topology element resources in the client layer
public interface LayerAdaptation {

  List<TopologyElement> getExposedClientEntities();

  List<Service> getUtilizedServices();

}