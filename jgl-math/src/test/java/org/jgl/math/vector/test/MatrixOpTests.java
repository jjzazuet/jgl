package org.jgl.math.vector.test;

import static org.jgl.math.matrix.Matrix4OpsGeom.*;

import org.jgl.math.matrix.Matrix4;
import org.jgl.math.vector.Vector3;
import org.junit.Test;

public class MatrixOpTests {
	
	@Test // TODO define unit tests for matrices
	public void matrixOpTests() {
	
		Matrix4 m = new Matrix4();
		Vector3 rotationAxis = new Vector3(1, 1, 1);
		
		for (int k = 0; k < 100; k++) {
			rotateLh(m, rotationAxis, k);
		}
	}
}
