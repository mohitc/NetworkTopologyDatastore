# Topology Web Service

This package implements a simple web-service container that loads a topology and
presents it over a web interface.

As part of the implementation, the topology `abilene.xml` located in the
resources folder is loaded in the `JettyResourceConfig` class and served over a
simple Jackson web service.

in order to test the implementation, go to the top-level project and run

```
mvn clean install
```

Once installed successfully, navigate to this project and run

```
mvn clean jetty:run-war
```

to launch a jetty server (configured by default on port 7474) and view the
topology at http://localhost:7474
