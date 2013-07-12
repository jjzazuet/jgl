package net.tribe7.math.matrix;

import static net.tribe7.math.Preconditions.*;
import static net.tribe7.math.matrix.Matrix4Ops.*;
import net.tribe7.math.vector.*;

public class Matrix4OpsGeom {

	public static void translateXyz(Matrix4 dst, Vector3 translation) {
		setIdentity(dst);
		dst.m30 = translation.x;
		dst.m31 = translation.y;
		dst.m32 = translation.z;
	}
	
	public static void scaleXyz(Matrix4 dst, Vector3 scale) {
		checkNoNulls(dst, scale);
		setIdentity(dst);
		dst.m00 = scale.x;
		dst.m11 = scale.y;
		dst.m22 = scale.z;
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
		
		dst.m00 = 1 -(2*yy) -(2*zz); dst.m10 =    (2*xy) -(2*wz); dst.m20 =    (2*xz) +(2*wy);
		dst.m01 =    (2*xy) +(2*wz); dst.m11 = 1 -(2*xx) -(2*zz); dst.m21 =    (2*yz) -(2*wx);
		dst.m02 =    (2*xz) -(2*wy); dst.m12 =    (2*yz) +(2*wx); dst.m22 = 1 -(2*xx) -(2*yy);
	}
}
