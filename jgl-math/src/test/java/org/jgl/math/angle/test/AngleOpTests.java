package org.jgl.math.angle.test;

import static org.junit.Assert.*;
import static org.jgl.math.angle.AngleOps.*;
import org.junit.Test;

public class AngleOpTests {

	@Test
	public void angleOpTests() {
		
		double fc1 = fullCircles(0.125);
		double fc2 = fullCircles(0.25);
		double fc3 = fullCircles(0.5);
		double fc4 = fullCircles(0.9);
		
		double a1 = Math.toDegrees(fc1);
		double a2 = Math.toDegrees(fc2);
		double a3 = Math.toDegrees(fc3);
		double a4 = Math.toRadians(fc4);

		assertTrue(a1 == 45.0);
		assertTrue(a2 == 90);
		assertTrue(a3 == 180);
		assertTrue(a4 ==  Math.toRadians(2 * Math.PI * 0.9));
	}
}
