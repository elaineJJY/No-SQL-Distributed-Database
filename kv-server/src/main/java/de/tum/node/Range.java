package de.tum.node;

import java.io.Serializable;

public class Range implements Serializable {
	private String from;
	private String to;

	public Range(String from, String to) {
		this.from = from;
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	@Override
	public String toString() {
		return from + "," + to;
	}
}
