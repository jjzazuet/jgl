package org.jgl.math.angle;

import static java.lang.Math.*;

public class AngleOps {

	public static final double TWO_PI  = Math.PI *  2;
	
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
