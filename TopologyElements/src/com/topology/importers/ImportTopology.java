package com.topology.importers;

import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.FileFormatException;
import com.topology.primitives.exception.TopologyException;

import java.io.IOException;

public interface ImportTopology {

  public void importFromFile(String fileName, TopologyManager manager) throws TopologyException, FileFormatException, IOException;

}
