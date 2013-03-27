package org.jgl.math.vector.test;

import static org.jgl.math.matrix.Matrix4OpsPersp.*;
import static org.jgl.math.matrix.Matrix4OpsGeom.*;

import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.Matrix4;
import org.jgl.math.vector.Vector3;
import org.junit.Test;


public class MatrixOpTests {
	
	@Test // TODO define unit tests for matrices
	public void matrix4OpGeomTests() {
	
		Matrix4 m = new Matrix4();
		Angle a = new Angle();
		Vector3 rotationAxis = new Vector3(1, 1, 1);
		
		for (int k = 0; k < 100; k++) {
			a.setDegrees(k);
			rotateLh(m, rotationAxis, a);
		}
	}
	
	@Test
	public void matrix4OpPerspectiveTests() {
		
		Matrix4 m = new Matrix4();
		Angle a = new Angle().setDegrees(48);
		double w = 800;
		double h = 600;
		
		perspectiveX(m, a, w/h, 1, 100);
	}
}
