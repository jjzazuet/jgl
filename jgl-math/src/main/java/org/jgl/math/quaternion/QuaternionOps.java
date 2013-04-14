package org.jgl.math.quaternion;

import static org.jgl.math.Preconditions.checkNoNulls;
import static org.jgl.math.vector.VectorOps.*;

import org.jgl.math.angle.Angle;
import org.jgl.math.vector.Vector4;

public class QuaternionOps {

	public static void conjugateQuaternion(Vector4 src, Vector4 dst) {
		checkNoNulls(src, dst);
		dst.set(-src.x, -src.y, -src.z, src.w);
	}

	/**
	 * @see <a href="http://www.arcsynthesis.org/gltut/Positioning/Tut08%20Quaternions.html">http://www.arcsynthesis.org/gltut/Positioning/Tut08%20Quaternions.html</a>
	 */
	public static void mulQuat(Vector4 a, Vector4 b, Vector4 dst) {
		
		checkNoNulls(a, b, dst);
		
		double x = (a.w * b.x) + (a.x * b.w) + (a.y * b.z) - (a.z * b.y);
		double y = (a.w * b.y) + (a.y * b.w) + (a.z * b.x) - (a.x * b.z);
		double z = (a.w * b.z) + (a.z * b.w) + (a.x * b.y) - (a.y * b.x);
		double w = (a.w * b.w) - (a.x * b.x) - (a.y * b.y) - (a.z * b.z);
	
	    dst.set(x, y, z, w);
	}

	public static void setQuaternion(Vector4 dst, Angle a) {
		checkNoNulls(dst, a);
		scale(dst, Math.sin(a.getRadians() / 2));
		dst.setW(Math.cos(a.getRadians() / 2));
	}
	
}
