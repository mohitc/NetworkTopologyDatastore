package io.github.mohitc.topology.test;

import io.github.mohitc.topology.impl.primitives.algorithm.PathAlgorithmTest;
import io.github.mohitc.topology.impl.primitives.connection.CrossConnectTest;
import io.github.mohitc.topology.impl.primitives.connection.LinkTest;
import io.github.mohitc.topology.impl.primitives.manager.TopologyManagerFactoryTest;
import io.github.mohitc.topology.impl.primitives.manager.TopologyManagerTest;
import io.github.mohitc.topology.impl.primitives.networkelement.NetworkElementTest;
import io.github.mohitc.topology.primitives.TopologyManager;
import io.github.mohitc.topology.primitives.TopologyManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractTopologyTest {

  private final Logger log = LoggerFactory.getLogger(this.getClass());
  public abstract void init();
  public abstract TopologyManagerFactory getTopologyManagerFactory();
  public abstract TopologyManager getTopologyManager();
  public abstract void close();

  @BeforeAll
  public void setup() {
    init();
  }

  @AfterAll
  public void teardown() {
    close();
  }

  /**
   * Concrete test classes should implement this method to provide a stream of TestCase objects.
   * @return A stream of TestCase objects to be executed.
   */
  protected Stream<TestCase> testCases() {
    return Stream.of(
      new PathAlgorithmTest(getTopologyManager()),
      new CrossConnectTest(getTopologyManager()),
      new LinkTest(getTopologyManager()),
      new TopologyManagerFactoryTest(getTopologyManagerFactory()),
      new TopologyManagerTest(getTopologyManager()),
      new NetworkElementTest(getTopologyManager())
    );
  }

  @ParameterizedTest(name = "{index}: {0}")
  @MethodSource("testCases")
  public void runTest(TestCase testCase) {
    if (log.isInfoEnabled()) {
      log.info("Running test case: {} - {}", testCase.getClass().getSimpleName(), testCase.getName());
    }
    testCase.executeTestCase();
  }
}
