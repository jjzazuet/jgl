package org.jgl.geom.bezier.test;

import static org.jgl.geom.bezier.BezierOps.*;

import java.util.Arrays;
import java.util.List;

import org.jgl.math.vector.Vector3;
import org.junit.Test;

public class BezierOpTest {

	@Test
	public void bezierOpTests() {

		Vector3[] _points = new Vector3[] { 
				new Vector3(-40.0f, -50.0f, -50.0f),
				new Vector3( 40.0f,  0.0f,  -60.0f),
				new Vector3( 60.0f,  30.0f,  50.0f),
				new Vector3(-20.0f,  50.0f,  55.0f),
				new Vector3(-30.0f,  30.0f,  0.0f),
				new Vector3(-60.0f,  4.0f,  -30.0f) 
		};

		List<Vector3> points = Arrays.asList(_points);
		List<Vector3> controlPoints = bezierCubicControlPointLoop(points);
		
		Vector3 r = new Vector3();
		bezierPointCubic(0, 
				controlPoints.get(0), 
				controlPoints.get(1), 
				controlPoints.get(2), 
				controlPoints.get(3), r);
		
		System.out.println();
	}

}
