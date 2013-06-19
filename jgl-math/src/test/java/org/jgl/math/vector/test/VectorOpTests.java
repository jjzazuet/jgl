package org.jgl.math.vector.test;

import static org.jgl.math.vector.VectorOps.*;

import org.jgl.math.vector.Vector3;
import org.junit.Test;

public class VectorOpTests {

	@Test
	public void testDistance() throws Exception {
		
		Vector3 p0 = new Vector3(1, 2, 3);
		Vector3 p1 = new Vector3(4, 5, 6);
		
		double distance = distance(p0, p1);
		
		System.out.println(distance);
	}
	
}
