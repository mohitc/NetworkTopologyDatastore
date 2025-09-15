package io.github.mohitc.topology.primitives.exception.properties;

import io.github.mohitc.topology.primitives.exception.TopologyException;

import java.io.Serial;

public class PropertyException extends TopologyException {

	/**
	 *
	 */
	@Serial
  private static final long serialVersionUID = 9136941828581969134L;

	public PropertyException(String message) {
		super(message);
	}

  public PropertyException(String message, Throwable cause) {
    super(message, cause);
  }

  public PropertyException(Throwable cause) {
    super(cause);
  }

}
