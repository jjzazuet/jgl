package org.jgl.math.angle;

public class Angle {

	private double radians;

	public double getRadians() { return radians; }
	public double getDegrees() { return Math.toDegrees(getRadians()); }
	
	public Angle setRadians(double radians) { 
		this.radians = radians;
		return this;
	}
	
	public Angle setDegrees(double degrees) { 
		this.radians = Math.toRadians(degrees);
		return this;
	}
}
