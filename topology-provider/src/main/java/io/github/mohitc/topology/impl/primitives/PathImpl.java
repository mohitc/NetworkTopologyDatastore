package io.github.mohitc.topology.impl.primitives;

import io.github.mohitc.topology.primitives.Connection;
import io.github.mohitc.topology.primitives.ConnectionPoint;
import io.github.mohitc.topology.primitives.Path;

import java.util.List;

public class PathImpl implements Path {

	private boolean strict;

	private boolean directed;

	private List<Connection> forwardConnectionSequence;

	private List<Connection> backwardConnectionSequence;

	private ConnectionPoint aEnd;

	private ConnectionPoint zEnd;

	@Override
	public boolean isStrict() {
		return strict;
	}

	@Override
	public void setStrict(boolean strict) {
		this.strict = strict;
	}

	@Override
	public boolean isDirected() {
		return directed;
	}

	@Override
	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	@Override
	public List<Connection> getForwardConnectionSequence() {
		return forwardConnectionSequence;
	}

	@Override
	public void setForwardConnectionSequence(List<Connection> sequence) {
		// TODO add code for validation
		this.forwardConnectionSequence = sequence;
	}

	@Override
	public List<Connection> getBackwardConnectionSequence() {
		return backwardConnectionSequence;
	}

	@Override
	public void setBackwardConnectionSequence(List<Connection> sequence) {
		// TODO add code for validation
		this.backwardConnectionSequence = sequence;
	}

  @Override
	public ConnectionPoint getaEnd() {
		return aEnd;
	}

  @Override
	public void setaEnd(ConnectionPoint aEnd) {
		this.aEnd = aEnd;
	}

  @Override
	public ConnectionPoint getzEnd() {
		return zEnd;
	}

  @Override
	public void setzEnd(ConnectionPoint zEnd) {
		this.zEnd = zEnd;
	}

  @Override
  public String toString(){
    return "Directed: " + directed + "forwardConn: " + forwardConnectionSequence + ", backwardConn: " + backwardConnectionSequence;
  }
}
