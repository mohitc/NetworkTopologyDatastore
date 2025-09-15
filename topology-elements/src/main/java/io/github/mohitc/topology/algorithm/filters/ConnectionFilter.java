package io.github.mohitc.topology.algorithm.filters;


import io.github.mohitc.topology.algorithm.PathSpan;
import io.github.mohitc.topology.algorithm.constraint.PathConstraint;
import io.github.mohitc.topology.primitives.Connection;


/**Connection Filters are used by the path algorithm to determine if a connection can be used to extend a path segment or not.
 * Inputs to the filter are the connection that is to used
 */

public interface ConnectionFilter {

  boolean filter(PathSpan pathSpan, Connection conn, PathConstraint constraint);
}
