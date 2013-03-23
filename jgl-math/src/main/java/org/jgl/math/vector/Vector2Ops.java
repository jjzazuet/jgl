package org.jgl.math.vector;

import static org.jgl.math.Preconditions.*;

public class Vector2Ops {

	public static void perpendicular(Vector2 src, Vector2 dst) {
		checkNoNulls(src, dst);
		dst.set(-src.y, src.x);
	}

	public static void perpendicular(Vector2 dst) {
		perpendicular(dst, dst);
	}
}
