package net.tribe7.geom.bezier;

import static net.tribe7.geom.bezier.BezierCurve4.*;
import static net.tribe7.geom.bezier.BezierOps.*;
import static net.tribe7.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.tribe7.math.vector.Vector3;

public class BezierCubicLoop {

	private List<BezierCurve4> pathCurves;
	
	public BezierCubicLoop(Vector3 ... targetLoopPoints) {
		checkNotNull(targetLoopPoints);
		checkArgument(targetLoopPoints.length > 0);
		setPoints(Arrays.asList(targetLoopPoints));
	}

	public BezierCubicLoop(List<Vector3> targetLoopPoints) {
		setPoints(targetLoopPoints);
	}

	public Vector3 pointAt(double t) {

		t = wrap(t);

		double pathCurveCount = pathCurves.size();
		double curveOffset = pathCurveCount * t;
		int targetCurve = (int) curveOffset;
		double curveLocalOffset = curveOffset - targetCurve;

		if (targetCurve == pathCurveCount) { // reached the end of the path loop
			targetCurve = 0;
		}

		return pathCurves.get(targetCurve).pointAt(curveLocalOffset);
	}

	public void setPoints(List<Vector3> points) {

		checkNotNull(points);
		checkArgument(!points.isEmpty());

		List<Vector3> rawControlPoints = bezierCubicControlPointLoop(points);
		int loopPoints = (points.size() * (DEGREE - 1)) + 1;

		checkArgument(rawControlPoints.size() == loopPoints);

		pathCurves = new ArrayList<BezierCurve4>();

		for (int k = 0; k < points.size(); k++) {

			int offset = k * (DEGREE - 1);

			BezierCurve4 curve = new BezierCurve4(
					rawControlPoints.get(offset), 
					rawControlPoints.get(offset + 1), 
					rawControlPoints.get(offset + 2), 
					rawControlPoints.get(offset + 3));

			pathCurves.add(curve);
		}
	}

	@Override
	public String toString() {
		return String.format("%s[%n%s]", 
				getClass().getSimpleName(), pathCurves);
	}
}
