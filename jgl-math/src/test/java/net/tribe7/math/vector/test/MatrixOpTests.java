package net.tribe7.math.vector.test;

import static net.tribe7.math.matrix.Matrix4OpsPersp.*;

import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.Matrix4;
import org.junit.Test;


public class MatrixOpTests {
	@Test
	public void matrix4OpPerspectiveTests() {

		Matrix4 m = new Matrix4();
		Angle a = new Angle().setDegrees(48);
		double w = 800;
		double h = 600;

		perspectiveX(m, a, w/h, 1, 100);
	}
}
