package com.topology.algorithm.filters;


import com.topology.primitives.Connection;


/**Connection Filters are used by the path algorithm to determine if a connection can be used to extend a path segment or not.
 * Inputs to the filter are the connection that is to used
 */

public interface ConnectionFilter {

  boolean filter(Connection conn);
}
