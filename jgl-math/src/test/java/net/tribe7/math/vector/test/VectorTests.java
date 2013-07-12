package net.tribe7.math.vector.test;

import static net.tribe7.math.vector.VectorOps.*;
import static net.tribe7.math.vector.Vector3Ops.*;
import static org.junit.Assert.*;

import net.tribe7.math.vector.*;
import org.junit.Test;

public class VectorTests {

	@Test
	public void testValues() {
	
		Vector4 v4 = new Vector4(1, 1, 1, 1);
		Vector4 v41 = new Vector4();
		Vector4 v4321 = new Vector4(0, 1, 2, 3);
		
		Vector3 v3 = new Vector3(1, 2, 3);
		Vector2 v2 = new Vector2(1, 2);
		
		assertFalse(v2.equals(v3));
		assertFalse(v3.equals(null));
		
		assertTrue(v4.x == 1);
		assertTrue(v4.y == 1);
		assertTrue(v4.z == 1);
		assertTrue(v4.w == 1);
		
		assertTrue(v41.x == 0);
		assertTrue(v41.y == 0);
		assertTrue(v41.z == 0);
		assertTrue(v41.w == 0);
		
		assertTrue(v4.values() != null && v4.values().length == 4);
		
		assertTrue(v4321.get(0) == 0);
		assertTrue(v4321.get(1) == 1);
		assertTrue(v4321.get(2) == 2);
		assertTrue(v4321.get(3) == 3);
		
		assertTrue(v4321.get(9999) == -1);
		
		v4321.set(0, 3);
		v4321.set(1, 2);
		v4321.set(2, 1);
		v4321.set(3, 0);
		
		v4321.set(9999, 9999);
		
		assertTrue(v4321.get(0) == 3);
		assertTrue(v4321.get(1) == 2);
		assertTrue(v4321.get(2) == 1);
		assertTrue(v4321.get(3) == 0);
		
		assertTrue(v4321.values().length == 4);
		assertTrue(v4.hashCode() == 4);
		
		v41.set(1, 1, 1, 1);
		
		assertTrue(v41.equals(v4));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDimensions() {
		Vector y = new Vector3();
		Vector x = new Vector2();
		add(x, y, new Vector3());
	}
	
	@Test
	public void testOps() {
		
		Vector3 a = new Vector3(1, 1, 1);
		Vector3 b = new Vector3(2, 2, 2);
		Vector3 c = new Vector3();
		
		add(a, b, c);
		assertTrue(c.hashCode() == 9);
		
		sub(a, b, c);
		assertTrue(c.hashCode() == -3);
		
		sub(b, a, c);
		assertTrue(c.hashCode() == 3);
		
		scale(c, 4);
		assertTrue(c.hashCode() == 12);
		
		a.set(5, 1, 4);
		b.set(-1, 0, 2);
		
		cross(a, b, c);
		assertTrue(dot(c, a) == 0);
		assertTrue(dot(c, b) == 0);
		
		c.set(8, 8, 8);
		assertTrue(length(c) > 13.85);
		normalize(c);
		
		assertTrue(c.x > 0.577350);
		assertTrue(c.y > 0.577350);
		assertTrue(c.z > 0.577350);
	}
}
