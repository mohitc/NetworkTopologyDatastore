package test.topology.fileimport;

import com.topology.impl.importers.sndlib.SNDLibImportTopology;
import com.topology.importers.ImportTopology;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.FileFormatException;
import com.topology.primitives.exception.TopologyException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.topology.primitives.manager.TopoManagerHelper;

import java.io.IOException;

import static org.junit.Assert.fail;

public class SNDLibImportTest {

  private static final Logger log = LoggerFactory.getLogger(SNDLibImportTest.class);

  private static TopologyManager getTopologyManager() {
    return TopoManagerHelper.getInstance();
  }

  @Test
  public void testImport() {
    ImportTopology importer = new SNDLibImportTopology();
    TopologyManager manager = getTopologyManager();
    try {
      importer.importFromFile(".//resources//import//sndlib//abilene.xml", manager);
    } catch (TopologyException e) {
      log.error("Topology Exception while parsing test.topology file", e);
      fail("Topology Exception while parsing test.topology file");
    } catch (FileFormatException e) {
      log.error("FileFormat Exception while parsing test.topology file", e);
      fail("FileFormat Exception while parsing test.topology file");
    } catch (IOException e) {
      log.error("IO Exception while parsing test.topology file", e);
      fail("IO Exception while parsing test.topology file");
    }

    log.info("Testing bidirectional path computation");

  }
}
