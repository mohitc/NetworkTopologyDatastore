package com.topology.impl.primitives;

import com.topology.primitives.Connection;
import com.topology.primitives.ConnectionPoint;
import com.topology.primitives.Path;

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

	public ConnectionPoint getaEnd() {
		return aEnd;
	}

	public void setaEnd(ConnectionPoint aEnd) {
		this.aEnd = aEnd;
	}

	public ConnectionPoint getzEnd() {
		return zEnd;
	}

	public void setzEnd(ConnectionPoint zEnd) {
		this.zEnd = zEnd;
	}

  public String toString(){
    return "Directed: " + directed + "forwardConn: " + forwardConnectionSequence + ", backwardConn: " + backwardConnectionSequence;
  }
}
