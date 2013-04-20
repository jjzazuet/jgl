package org.jgl.geom.bezier;

import static java.lang.Math.*;
import static org.jgl.math.vector.VectorOps.*;
import static com.google.common.base.Preconditions.*;
import static org.jgl.math.Preconditions.*;

import java.util.*;
import org.jgl.math.vector.Vector3;

public class BezierOps {

	public static void bezierPointCubic(double t, 
			Vector3 p0, Vector3 p1,
			Vector3 p2, Vector3 p3, Vector3 dst) {

		t = wrap(t);
		
		checkNoNulls(p0, p1, p2, p3, dst);
		
		double u = 1 - t;
		double tt = t * t;
		double uu = u * u;
		double uuu = uu * u;
		double ttt = tt * t;

		dst.set(p0.x * uuu,  p0.y * uuu,  p0.z * uuu);
		
		dst.x += 3 * uu * t * p1.x;
		dst.y += 3 * uu * t * p1.y;
		dst.z += 3 * uu * t * p1.z;
		
		dst.x += 3 * u * tt * p2.x;
		dst.y += 3 * u * tt * p2.y;
		dst.z += 3 * u * tt * p2.z;

		dst.x += ttt * p3.x;
		dst.y += ttt * p3.y;
		dst.z += ttt * p3.z;
	}
	
	public static List<Vector3> bezierCubicControlPointLoop(List<Vector3> points) {

		int i = 0, n = points.size(), size = n * 3 + 1;
		double r = 1.0 / 3.0;

		checkState(n != 0);

		List<Vector3> result = new ArrayList<Vector3>(size);

		for (int k = 0; k < size; k++) {
			result.add(new Vector3());
		}

		Iterator<Vector3> irt = result.iterator();
		Vector3 ir = irt.next();
		Vector3 last = result.get(result.size() - 1);

		while (i != n) {

			int a = (n + i - 1) % n;
			int b = i;
			int c = (i + 1) % n;
			int d = (i + 2) % n;

			checkState(ir != last);

			ir.set(points.get(b));
			ir = irt.next();

			checkState(ir != last);

			sub(points.get(c), points.get(a), ir);
			scale(ir, r);
			add(ir, points.get(b), ir);
			ir = irt.next();

			checkState(ir != last);

			sub(points.get(b), points.get(d), ir);
			scale(ir, r);
			add(ir, points.get(c), ir);
			ir = irt.next();

			++i;
		}

		ir.set(points.get(0));
		checkState(ir == last);
		return result;
	}
	
	public static double wrap(double t) {
		if (t < 0) { t += floor(abs(t)) + 1; } 
		else if (t > 1) { t -= floor(t); }
		checkState(t >= 0 && t <= 1);
		return t;
	}
}
