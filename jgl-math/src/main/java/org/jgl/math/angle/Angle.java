package org.jgl.math.angle;

public class Angle {

	public static final double TWO_PI  = Math.PI * 2;
	
	private double radians;

	public double getRadians() { return radians; }
	public double getDegrees() { return Math.toDegrees(getRadians()); }
	
	public Angle setFullCircles(double value) {
		this.radians = value * TWO_PI;
		return this;
	}
	
	public Angle setRadians(double radians) { 
		this.radians = radians;
		return this;
	}
	
	public Angle setDegrees(double degrees) { 
		this.radians = Math.toRadians(degrees);
		return this;
	}
	
	@Override
	public String toString() {
		return String.format("%s[%s rad, %s deg]", 
				getClass().getSimpleName(),
				getRadians(), getDegrees());
	}
}
