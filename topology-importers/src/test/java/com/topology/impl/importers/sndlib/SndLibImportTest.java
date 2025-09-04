package com.topology.impl.importers.sndlib;

import com.topology.impl.primitives.LinkImpl;
import com.topology.impl.primitives.NetworkElementImpl;
import com.topology.impl.primitives.TopologyManagerImpl;
import com.topology.importers.ImportTopology;
import com.topology.primitives.TopologyManager;
import com.topology.primitives.exception.FileFormatException;
import com.topology.primitives.exception.TopologyException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SndLibImportTest {

  private static final Logger log = LoggerFactory.getLogger(SndLibImportTest.class);

  public enum SNDLibSource {
    ABILENE ("abilene.xml", 12, 15),
    ATLANTA("atlanta.xml", 15, 22),
    JANOS_US("janos-us.xml", 26, 42),
    JANOS_US_DUPLICATES("janos-us.xml", 26, 84, false),
    ZIB54("zib54.xml", 54, 80),
    ZIB54_DUPLICATES("zib54.xml", 54, 81, false);

    SNDLibSource(String fileName, int nodeNum, int linkNum) {
      this.fileName = fileName;
      this.nodeNum = nodeNum;
      this.linkNum = linkNum;
      this.removeDuplicaties = true;
    }

    SNDLibSource(String fileName, int nodeNum, int linkNum, boolean removeDuplicaties) {
      this.fileName = fileName;
      this.nodeNum = nodeNum;
      this.linkNum = linkNum;
      this.removeDuplicaties = removeDuplicaties;
    }


    final String fileName;
    final int nodeNum;
    final int linkNum;
    final boolean removeDuplicaties;
  }

  @ParameterizedTest(name = "{index} => message=''{0}''")
  @EnumSource(SndLibImportTest.SNDLibSource.class)
  public void testImport(SNDLibSource source) {
    ImportTopology importer = new SNDLibImportTopology(source.removeDuplicaties);
    TopologyManager manager = new TopologyManagerImpl(source.fileName);
    try {
      log.info("Loading topology from file {}", source.fileName);
      importer.importFromFile(this.getClass().getClassLoader().getResource(source.fileName).getFile(), manager);
      assertEquals(manager.getAllElements(NetworkElementImpl.class).size(), source.nodeNum, "Expected " + source.nodeNum + " nodes in " + source.fileName +
          ". Parsed Nodes = " + manager.getAllElements(NetworkElementImpl.class).size());
      assertEquals(manager.getAllElements(LinkImpl.class).size(), source.linkNum, "Expected " + source.linkNum + " links in " + source.fileName +
          ". Parsed Links = " + manager.getAllElements(LinkImpl.class).size());
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

    manager.removeAllElements();
  }
}
