package org.jgl.geom.bezier.test;

import static org.jgl.geom.bezier.BezierOps.*;

import java.util.Arrays;
import java.util.List;

import org.jgl.geom.bezier.BezierCubicLoop;
import org.jgl.geom.bezier.BezierCurve4;
import org.jgl.math.vector.Vector3;
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

	public static final List<Vector3> points = Arrays.asList(_points);

	@Test
	public void bezierCurveTest() {

		List<Vector3> controlPoints = bezierCubicControlPointLoop(points);
		
		BezierCurve4 c0 = new BezierCurve4(
				controlPoints.get(0),
				controlPoints.get(1),
				controlPoints.get(2),
				controlPoints.get(3));
		
		System.out.print(c0.pointAt(0.0));
		System.out.print(c0.pointAt(0.5));
		System.out.print(c0.pointAt(1));
	}
	
	@Test
	public void bezierLoopTest() {
		
		BezierCubicLoop loop = new BezierCubicLoop(points);
		
		System.out.println(loop);
	}
}
