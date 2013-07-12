package net.tribe7.math.matrix.test;

import java.nio.FloatBuffer;

import static org.junit.Assert.*;
import static net.tribe7.math.matrix.Matrix2Ops.*;
import net.tribe7.math.matrix.Matrix2;
import org.junit.Test;

public class Matrix2OpTests {

	@Test
	public void matrix2OpTests() {
		
		Matrix2 m0 = new Matrix2();
		Matrix2 m1 = new Matrix2();
		FloatBuffer b = FloatBuffer.allocate(Matrix2.COMPONENT_SIZE * 2);

		float [] rawData = new float [Matrix2.COMPONENT_SIZE * 2];
		
		m1.m00 = 1; m1.m10 = 2;
		m1.m01 = 4; m1.m11 = 5; 
		
		setIdentity(m0);
		storeColMaj(b, m1, m0);
		b.clear();
		b.get(rawData);
		
		assertTrue(b.capacity() == 8);
		assertTrue(b.remaining() == 0);

		assertTrue(rawData[0] == 1);
		assertTrue(rawData[1] == 4);
		assertTrue(rawData[2] == 2);
		assertTrue(rawData[3] == 5);
		
		assertTrue(rawData[4] == 1);
		assertTrue(rawData[5] == 0);
		assertTrue(rawData[6] == 0);
		assertTrue(rawData[7] == 1);
	}
}
