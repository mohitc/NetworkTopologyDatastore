package io.github.mohitc.topology.primitives.exception.resource;

import io.github.mohitc.topology.primitives.exception.TopologyException;

import java.io.Serial;

public class ResourceException extends TopologyException {

	/**
	 *
	 */
	@Serial
  private static final long serialVersionUID = 6187589009790999402L;

	public ResourceException(String message) {
		super(message);
	}

  public ResourceException(String message, Throwable cause) {
    super(message, cause);
  }

}
