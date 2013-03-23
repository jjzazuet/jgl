package org.jgl.math.angle;

public class AngleOps {

	public static final double HALF_PI = Math.PI * .5;
	public static final double TWO_PI  = Math.PI *  2;
	
	/** 
	 * Creates a new angle from a value in "full circles" (i.e. 360 degrees).
	 * 
	 * This function creates a new angle from a value specifying the fraction
	 * of a full 360 degree (2 pi radians) angle. For example the following
	 * is true:
	 * 
	 * <pre>
	 *  FullCircles(0.125) == Degrees(45);
	 *  FullCircles(0.25) == Degrees(90);
	 *  FullCircles(0.25) == Radians(PI / 2);
	 *  FullCircles(0.5) == Degrees(180);
	 *  FullCircles(0.5) == Radians(PI);
	 *  FullCircles(0.9) == Radians(2 * PI * 0.9); 
	 * </pre>
	 * @param value a value in 360-degree units
	 */
	@OutRadians
	public static final double fullCircles(@InDegrees double value) {
		return value * TWO_PI;
	}
}
