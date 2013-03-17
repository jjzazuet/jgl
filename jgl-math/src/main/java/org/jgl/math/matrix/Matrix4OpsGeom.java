package org.jgl.math.matrix;

import static org.jgl.math.Preconditions.*;

import static java.lang.Math.*;
import static org.jgl.math.matrix.Matrix4Ops.*;
import static org.jgl.math.vector.VectorOps.*;

import org.jgl.math.vector.Vector3;

public class Matrix4OpsGeom {

	public static void translateXyz(Matrix4 dst, Vector3 translation) {
		setIdentity(dst);
		dst.m03 = translation.x;
		dst.m13 = translation.y;
		dst.m23 = translation.z;
	}
	
	public static void scaleXyz(Matrix4 dst, Vector3 scale) {
		checkNoNulls(dst, scale);
		dst.m00 = scale.x;
		dst.m11 = scale.y;
		dst.m22 = scale.z;
		dst.m33 = 1;
	}

	public static void rotateXLh(Matrix4 dst, double xDeg) {
		setIdentity(dst);
		double xRadCos = cos(toRadians(xDeg));
		double xRadSin = sin(toRadians(xDeg));
		dst.m11 = xRadCos; dst.m12 = -xRadSin;
		dst.m21 = xRadSin; dst.m22 =  xRadCos; 
	}

	public static void rotateYLh(Matrix4 dst, double yDeg) {
		setIdentity(dst);
		double yRadCos = cos(toRadians(yDeg));
		double yRadSin = sin(toRadians(yDeg));
		dst.m00 =  yRadCos; dst.m02 = yRadSin;
		dst.m20 = -yRadSin; dst.m22 = yRadCos; 		
	}

	public static void rotateZLh(Matrix4 dst, double zDeg) {
		setIdentity(dst);
		zDeg = toRadians(zDeg);
		double zRadCos = cos(zDeg);
		double zRadSin = sin(zDeg);
		dst.m00 = zRadCos; dst.m01 = -zRadSin;
		dst.m10 = zRadSin; dst.m11 =  zRadCos; 		
	}

	// http://www.cprogramming.com/tutorial/3d/rotation.html
	public static void rotateLh(Matrix4 dst, Vector3 axis, double angDeg) {
		
		checkNoNulls(axis, dst);
		
		Vector3 srcNorm = new Vector3();
		
		normalize(axis, srcNorm);
		setIdentity(dst);
		
		double rotRad = toRadians(angDeg);
		double rotSin = sin(rotRad);
		double rotCos = cos(rotRad);
		double oneMinusRotCos = 1 - rotCos;
		double x = srcNorm.x, y = srcNorm.y, z = srcNorm.z;
		double xx = x*x, xy = x*y, xz = x*z, yy = y*y, yz = y*z, zz = z*z;
		
		dst.m00 = xx * oneMinusRotCos + rotCos;
		dst.m01 = xy * oneMinusRotCos - z * rotSin;
		dst.m02 = xz * oneMinusRotCos + y * rotSin;
		
		dst.m10 = xy * oneMinusRotCos + z * rotSin;
		dst.m11 = yy * oneMinusRotCos + rotCos;
		dst.m12 = yz * oneMinusRotCos - x * rotSin;

		dst.m20 = xz * oneMinusRotCos - y*rotSin;
		dst.m21 = yz * oneMinusRotCos + x*rotSin;
		dst.m22 = zz * oneMinusRotCos + rotCos;
	}
}
