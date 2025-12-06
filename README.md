# Network Topology Datastore

A Java project to import and operate on network topologies, with some
implementations to have the topologies available as an in-memory representation
or as a persistent representation in a database.

## Modules

This project is divided into the following modules:

* **topology-elements**: Contains the core interfaces and classes for
  representing network topologies.
* **topology-importers**: Provides functionality to import topologies from
  various external formats, such as SNDLib.
* **topology-provider**: An in-memory implementation of the topology datastore.
* **persistent-topology-provider**: A persistent implementation of the topology
  datastore using a PostgreSQL database.
* **topology-notification-handlers**: Provides handlers for processing topology
  notifications.
* **topology-web-service**: A simple web service that exposes the topology data
  over a REST API.
* **topology-provider-tests**: A test suite for topology providers.

## Getting Started

To build the project, run the following command from the root directory:

```
mvn clean install
```

### Web Service

The `topology-web-service` module provides a simple web interface to view the
topology. To run the web service, navigate to the `topology-web-service`
directory and run the following command:

```
mvn clean jetty:run-war
```

This will start a Jetty server on port 7474. You can then view the topology
at http://localhost:7474.

## Publishing to Maven Central

This project is configured for publishing to Maven Central. To publish the
artifacts, create a release on GitHub. This will trigger a GitHub Actions
workflow that will build, sign, and publish the artifacts to Maven Central.

You can also trigger the workflow manually from the Actions tab in the GitHub
repository.
