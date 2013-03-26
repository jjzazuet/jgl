package org.jgl.math.angle;

import static java.lang.Math.*;

// TODO is this class really necessary??
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
	
	@OutRadians
	public static final double degrees(@InDegrees double value) {
		return toRadians(value);
	}
	
	/** 
	 * Returns a value on a sine wave at the specified point.
	 * 
	 * This function returns the value of sin(2.PI.@p t), i.e.
	 * integer values of @p t are the ends of the previous full
	 * sine wave and the beginning of the next "iteration".
	 * 
	 * The following is true:
	 * 
	 *  <pre>
	 *  SineWave(t) == sin(2.0*PI*t);
	 *  SineWave(0.00) ==  0.0;
	 *  SineWave(0.25) ==  1.0;
	 *  SineWave(0.50) ==  0.0;
	 *  SineWave(0.75) == -1.0;
	 *  SineWave(1.00) ==  0.0;
	 *  </pre>
	 *
	 *  @param t the point for which to calculate the value on the wave.
	 */
	public static final double sineWave(double t) {
		return sin(TWO_PI * t);
	}
}
