package net.tribe7.math.vector;

import static net.tribe7.math.Preconditions.*;

public class Vector4Ops {

	public static void mulVec(Vector4 q, Vector3 v, Vector4 dst) {
		
		checkNoNulls(q, v, dst);
		
		double w = - (q.x * v.x) - (q.y * v.y) - (q.z * v.z);
		double x =   (q.w * v.x) + (q.y * v.z) - (q.z * v.y);
		double y =   (q.w * v.y) + (q.z * v.x) - (q.x * v.z);
		double z =   (q.w * v.z) + (q.x * v.y) - (q.y * v.x);

	    dst.set(x, y, z, w);
	}
}
