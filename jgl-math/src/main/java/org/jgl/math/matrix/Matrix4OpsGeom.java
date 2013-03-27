package org.jgl.math.matrix;

import static org.jgl.math.Preconditions.*;
import static org.jgl.math.matrix.Matrix4Ops.*;
import static org.jgl.math.vector.VectorOps.*;
import static org.jgl.math.angle.AngleOps.*;

import org.jgl.math.angle.Angle;
import org.jgl.math.vector.Vector3;

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

	public static void rotateXLh(Matrix4 dst, Angle a) {
		setIdentity(dst);
		double cosx = cos(a);
		double sinx = sin(a);
		dst.m11 = cosx; dst.m21 = -sinx;
		dst.m12 = sinx; dst.m22 =  cosx; 
	}

	public static void rotateYLh(Matrix4 dst, Angle a) {
		setIdentity(dst);
		double cosy = cos(a);
		double siny = sin(a);
		dst.m00 =  cosy; dst.m20 = siny;
		dst.m02 = -siny; dst.m22 = cosy; 		
	}

	public static void rotateZLh(Matrix4 dst, Angle a) {
		setIdentity(dst);
		double cosz = cos(a);
		double sinz = sin(a);
		dst.m00 = cosz; dst.m10 = -sinz;
		dst.m01 = sinz; dst.m11 =  cosz; 		
	}

	// http://www.cprogramming.com/tutorial/3d/rotation.html
	public static void rotateLh(Matrix4 dst, Vector3 axis, Angle d) {
		
		checkNoNulls(axis, dst);
		
		Vector3 a = new Vector3();
		
		normalize(axis, a);
		setIdentity(dst);
		
		double sf = sin(d);
		double cf = cos(d);
		double _cf = 1 - cf;
		double x = a.x, y = a.y, z = a.z;
		double xx = x*x, xy = x*y, xz = x*z, yy = y*y, yz = y*z, zz = z*z;

		dst.m00 = cf + xx*_cf;   dst.m10 = xy*_cf - z*sf; dst.m20 = xz*_cf + y*sf;
		dst.m01 = xy*_cf + z*sf; dst.m11 = cf + yy*_cf;   dst.m21 = yz*_cf - x*sf;
		dst.m02 = xz*_cf - y*sf; dst.m12 = yz*_cf + x*sf; dst.m22 = cf + zz*_cf;
	}
}
