package org.jgl.math.angle.test;

import static org.junit.Assert.*;
import org.jgl.math.angle.Angle;
import org.junit.Test;

public class AngleOpTests {

	@Test
	public void angleOpTests() {
		
		Angle a = new Angle();
		
		double a1 = a.setFullCircles(0.125).getDegrees();
		double a2 = a.setFullCircles(0.25).getDegrees();
		double a3 = a.setFullCircles(0.5).getDegrees();
		double a4 = a.setFullCircles(0.9).getRadians();
		
		assertTrue(a1 == 45.0);
		assertTrue(a2 == 90);
		assertTrue(a3 == 180);
		assertTrue(a4 == 2 * Math.PI * 0.9);
	}
}
