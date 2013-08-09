package net.tribe7.math.matrix;

import static net.tribe7.math.Preconditions.*;
import static net.tribe7.math.matrix.MatrixOps.*;
import net.tribe7.math.vector.*;

public class Matrix4OpsGeom {

	public static void translateXyz(Matrix4 dst, Vector3 translation) {
		setIdentity(dst);
		dst.m(3,0, translation.x);
		dst.m(3,1, translation.y);
		dst.m(3,2, translation.z);
	}

	public static void scaleXyz(Matrix4 dst, Vector3 scale) {
		checkNoNulls(dst, scale);
		setIdentity(dst);
		dst.m(0,0, scale.x);
		dst.m(1,1, scale.y);
		dst.m(2,2, scale.z);
	}

	/** 
	 * @see 
	 *   <a href="http://www.arcsynthesis.org/gltut/Positioning/Tut08%20Quaternions.html">
	 *     http://www.arcsynthesis.org/gltut/Positioning/Tut08%20Quaternions.html</a> 
	 **/
	public static void fromQuaternion(Matrix4 dst, Vector4 q) {
	
		checkNoNulls(dst, q);
		
		double xx = q.x * q.x;
		double xy = q.x * q.y;
		double xz = q.x * q.z;

		double yy = q.y * q.y;
		double yz = q.y * q.z;

		double zz = q.z * q.z;

		double wx = q.w * q.x;
		double wy = q.w * q.y;
		double wz = q.w * q.z;

		setIdentity(dst);

		dst.m(0,0, 1 -(2*yy) -(2*zz)); dst.m(1,0,    (2*xy) -(2*wz)); dst.m(2,0,    (2*xz) +(2*wy));
		dst.m(0,1,    (2*xy) +(2*wz)); dst.m(1,1, 1 -(2*xx) -(2*zz)); dst.m(2,1,    (2*yz) -(2*wx));
		dst.m(0,2,    (2*xz) -(2*wy)); dst.m(1,2,    (2*yz) +(2*wx)); dst.m(2,2, 1 -(2*xx) -(2*yy));
	}
}
