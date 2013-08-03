package net.tribe7.math.angle;

public class Angle {

	public static final double TWO_PI  = Math.PI * 2;
	public static final double HALF_PI = Math.PI / 2;
	private double radians;

	public double getRadians() { return radians; }
	public double getDegrees() { return Math.toDegrees(getRadians()); }

	/**
	 * This function creates a new angle from a value specifying the fraction of
	 * a full 360 degree (2 pi radians) angle. For example the following is
	 * true:
	 * 
	 * <pre>
	 *  FullCircles(0.125) == Degrees(45);
	 *  FullCircles(0.25) == Degrees(90);
	 *  FullCircles(0.25) == Radians(PI / 2);
	 *  FullCircles(0.5) == Degrees(180);
	 *  FullCircles(0.5) == Radians(PI);
	 *  FullCircles(0.9) == Radians(2 * PI * 0.9);
	 * </pre>
	 * 
	 * @param value a value in 360-degree units
	 */
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

	public Angle setRightAngles(double value) {
		return setRadians(value * HALF_PI);
	}

	public final double cos() { return Math.cos(getRadians()); }
	public final double sin() { return Math.sin(getRadians()); }

	@Override
	public String toString() {
		return String.format("%s[%s rad, %s deg]", 
				getClass().getSimpleName(),
				getRadians(), getDegrees());
	}
}
