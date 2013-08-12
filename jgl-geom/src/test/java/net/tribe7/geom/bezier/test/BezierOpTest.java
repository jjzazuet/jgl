package net.tribe7.geom.bezier.test;

import static net.tribe7.geom.bezier.BezierOps.*;

import java.util.Arrays;
import java.util.List;

import net.tribe7.geom.bezier.BezierCubicLoop;
import net.tribe7.geom.bezier.BezierCurve4;
import net.tribe7.math.vector.Vector3;
import org.junit.Test;

public class BezierOpTest {

	public static final Vector3[] _points = new Vector3[] { 
			new Vector3(-40.0f, -50.0f, -50.0f),
			new Vector3( 40.0f,  0.0f,  -60.0f),
			new Vector3( 60.0f,  30.0f,  50.0f),
			new Vector3(-20.0f,  50.0f,  55.0f),
			new Vector3(-30.0f,  30.0f,  0.0f),
			new Vector3(-60.0f,  4.0f,  -30.0f)
	};
	
	public static final Vector3[] _fourPoints = new Vector3[] {
			new Vector3(0, 0, 0),
			new Vector3(1, 1, 1),
			new Vector3(2, 2, 2),
			new Vector3(3, 3, 3)
	};
	
	public static final List<Vector3> points = Arrays.asList(_points);
	public static final List<Vector3> fourPoints = Arrays.asList(_fourPoints);
	
	@Test
	public void bezierCurveTest() {

		List<Vector3> controlPoints = bezierCubicControlPointLoop(points, (double) 1/3);

		BezierCurve4 c0 = new BezierCurve4(
				controlPoints.get(0),
				controlPoints.get(1),
				controlPoints.get(2),
				controlPoints.get(3));

		Vector3 p = new Vector3();

		c0.pointAt(0.0, p);
		System.out.print(p);

		c0.pointAt(0.5, p);
		System.out.print(p);

		c0.pointAt(1, p);	
		System.out.print(p);
	}
	
	@Test
	public void bezierLoopTest() {

		double iterations = 8;
		double deltaStep = 0.25;

		BezierCubicLoop loop = new BezierCubicLoop(_fourPoints);
		Vector3 point = new Vector3();

		for (double d = 0; d <= iterations; d += deltaStep) {
			loop.pointAt(d, point);
			System.out.printf("[%.3f], %s", d, point);
		}
	}
}
