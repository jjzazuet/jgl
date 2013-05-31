package org.jgl.math.vector;

import java.util.Arrays;

public abstract class Vector {
	
	public static final int VALUE_MINUS_ONE = -1;
	public double x = 0;

	protected Vector() {}
	protected Vector(double x) { setX(x); }

	public void setX(double x) { this.x = x; }
	public void set(double x) { setX(x); }
	
	protected double get(int index) {
		if (index == 0) { return x; }
		return VALUE_MINUS_ONE;
	}

	protected void set(int index, double value) {
		if (index == 0) { setX(value); }
	}
	
	public void set(Vector v) {
		for (int k = 0; k < v.length(); k++) {
			set(k, v.get(k));
		}
	}
	
	protected double[] values() { return new double [] {x}; }
	public int length() { return 1; }
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		double [] values = values();
		sb.append(getClass().getSimpleName()).append("[ ");
		
		for (int k = 0; k < values.length; k++) {
			sb.append(values[k]).append(' ');
		}
		
		sb.append("]\n");
		return sb.toString();
	}
	
	public boolean equals(Object o) {
		
		boolean equals = false;
		
		if (o != null && o instanceof Vector) {
			Vector v = (Vector) o;
			equals = Arrays.equals(values(), v.values());
		}
		
		return equals;
	}
	
	public int hashCode() {
		int hc = 0;
		double [] myValues = values();
		for (int k = 0; k < myValues.length; k++) {
			hc += (int) myValues[k];
		}
		return hc;
	}
}
