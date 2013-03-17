package org.jgl.math.vector;

import static org.jgl.math.Preconditions.*;

public class Vector4Ops {

	public static void conjugateQuaternion(Vector4 src, Vector4 dst) {
		checkNoNulls(src, dst);
		dst.set(-src.x, -src.y, -src.z, src.w);
	}
	
	public static void mulQuat(Vector4 l, Vector4 r, Vector4 dst) {
		
		checkNoNulls(l, r, dst);
		
	    double w = (l.w * r.w) - (l.x * r.x) - (l.y * r.y) - (l.z * r.z);
	    double x = (l.x * r.w) + (l.w * r.x) + (l.y * r.z) - (l.z * r.y);
	    double y = (l.y * r.w) + (l.w * r.y) + (l.z * r.x) - (l.x * r.z);
	    double z = (l.z * r.w) + (l.w * r.z) + (l.x * r.y) - (l.y * r.x);

	    dst.set(x, y, z, w);
	}
	
	public static void mulVec(Vector4 q, Vector3 v, Vector4 dst) {
		
		checkNoNulls(q, v, dst);
		
	    double w = - (q.x * v.x) - (q.y * v.y) - (q.z * v.z);
	    double x =   (q.w * v.x) + (q.y * v.z) - (q.z * v.y);
	    double y =   (q.w * v.y) + (q.z * v.x) - (q.x * v.z);
	    double z =   (q.w * v.z) + (q.x * v.y) - (q.y * v.x);

	    dst.set(x, y, z, w);
	}
}
