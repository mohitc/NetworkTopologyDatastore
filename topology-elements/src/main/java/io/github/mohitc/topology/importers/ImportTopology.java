package io.github.mohitc.topology.importers;

import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.exception.FileFormatException;
import io.github.mohitc.topology.primitives.exception.TopologyException;

import java.io.IOException;

public interface ImportTopology {

  void importFromFile(String fileName, TopologyManager manager) throws TopologyException, FileFormatException, IOException;

}
