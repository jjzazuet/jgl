package org.jgl.geom.bezier;

import static org.jgl.geom.bezier.BezierOps.*;
import static com.google.common.base.Preconditions.*;
import java.util.List;

import org.jgl.math.vector.Vector3;

public class BezierCubicLoop {

	private List<Vector3> points;
	
	public BezierCubicLoop(List<Vector3> loopPathPoints) {
		setPoints(loopPathPoints);
	}

	
	
	public void setPoints(List<Vector3> points) {
		this.points = bezierCubicControlPointLoop(checkNotNull(points));
	}
}
