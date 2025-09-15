package io.github.mohitc.topology.primitives.exception;

import java.io.Serial;

public class TopologyException extends Exception{

	/**
	 *
	 */
	@Serial
	private static final long serialVersionUID = 1370403232109717641L;

	private final String message;

	public TopologyException (String message) {
		this.message = message;
	}

  public TopologyException(String message, Throwable cause) {
    super(message, cause);
    this.message = message;
  }

  public TopologyException(Throwable cause) {
    super(cause);
    this.message = cause.getMessage();
  }

  @Override
	public String getMessage() {
		return message;
	}
}
