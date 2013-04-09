package org.jgl.math.vector;

import static org.jgl.math.Preconditions.*;

public class Vector3Ops {

	public static void cross(Vector3 l, Vector3 r, Vector3 dst) {

		checkNoNulls(l, r, dst);

		double cx = l.y * r.z - l.z * r.y;
		double cy = l.z * r.x - l.x * r.z;
		double cz = l.x * r.y - l.y * r.x;

		dst.set(cx, cy, cz);
	}
}
