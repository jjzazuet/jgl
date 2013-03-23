package org.jgl.math.matrix.test;

import java.nio.FloatBuffer;

import static org.junit.Assert.*;
import static org.jgl.math.matrix.Matrix2Ops.*;
import org.jgl.math.matrix.Matrix2;
import org.junit.Test;

public class Matrix2OpTests {

	@Test
	public void matrix2OpTests() {
		
		Matrix2 m0 = new Matrix2();
		Matrix2 m1 = new Matrix2();
		FloatBuffer b = FloatBuffer.allocate(Matrix2.COMPONENT_SIZE * 2);
		float [] rawData = new float [Matrix2.COMPONENT_SIZE * 2];
		
		m1.m00 = 1; m1.m01 = 1; m1.m10 = 1; m1.m11 = 1;
		
		store(b, m0, m1);
		b.clear();
		b.get(rawData);
		
		assertTrue(b.capacity() == 8);
		assertTrue(b.remaining() == 0);
		assertTrue(rawData[3] == 0);
		assertTrue(rawData[4] == 1);
		assertTrue(rawData[7] == 1);
	}
}
