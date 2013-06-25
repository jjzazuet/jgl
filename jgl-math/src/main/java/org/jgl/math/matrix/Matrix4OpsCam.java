package org.jgl.math.matrix;

import static com.google.common.base.Preconditions.*;
import static org.jgl.math.Preconditions.*;

import static java.lang.Math.*;
import static org.jgl.math.matrix.Matrix4Ops.*;
import static org.jgl.math.vector.VectorOps.*;
import static org.jgl.math.vector.Vector3Ops.*;

import org.jgl.math.angle.Angle;
import org.jgl.math.vector.Vector3;

public class Matrix4OpsCam {

	public static void lookAt(Matrix4 dst, Vector3 eye, Vector3 target) {
		
		checkNoNulls(eye, target);
		checkArgument(!eye.equals(target));
		
		Vector3 x = new Vector3();
		Vector3 y = new Vector3();
		Vector3 z = new Vector3();
		Vector3 t = new Vector3();
		
		sub(eye, target, z);
		normalize(z);
		
		double zx = z.x, zz = z.z;
		
		t.set(zz, 0, -zx);
		cross(z, t, y);
		normalize(y);
		cross(y, z, x);
		setIdentity(dst);
		
		dst.m00 = x.x; dst.m10 = x.y; dst.m20 = x.z; dst.m30 = -dot(eye, x);
		dst.m01 = y.x; dst.m11 = y.y; dst.m21 = y.z; dst.m31 = -dot(eye, y);
		dst.m02 = z.x; dst.m12 = z.y; dst.m22 = z.z; dst.m32 = -dot(eye, z);
	}
	
	public static void orbit(Matrix4 dst, Vector3 target, 
			double radius, Angle azimuth, Angle elevation) {
		
		checkNoNulls(dst, target);
				
		Vector3 z = new Vector3(
				elevation.cos() * azimuth.cos(), 
				elevation.sin(), 
				elevation.cos() * -azimuth.sin());
		
		Vector3 x = new Vector3(-azimuth.sin(), 0, -azimuth.cos());
		Vector3 y = new Vector3();
		
		cross(z, x, y);
		setIdentity(dst);
		
		dst.m00 = x.x; dst.m10 = x.y; dst.m20 = x.z; dst.m30 = dot(x, z) * -radius - dot(x, target);
		dst.m01 = y.x; dst.m11 = y.y; dst.m21 = y.z; dst.m31 = dot(y, z) * -radius - dot(y, target);
		dst.m02 = z.x; dst.m12 = z.y; dst.m22 = z.z; dst.m32 = dot(z, z) * -radius - dot(z, target);
	}
	
	public static void pitch(Matrix4 dst, Angle a) {
		
		checkNotNull(dst);
		double cosx = cos(-a.getRadians());
		double sinx = sin(-a.getRadians());
		setIdentity(dst);
		
		dst.m21 = cosx; dst.m31 = -sinx;
		dst.m22 = sinx; dst.m32 =  cosx;
	}
	
	public static void yaw(Matrix4 dst, Angle a) {
		
		checkNotNull(dst);
		double cosx = cos(-a.getRadians());
		double sinx = sin(-a.getRadians());
		setIdentity(dst);

		dst.m00 =  cosx; dst.m20 = sinx;
		dst.m02 = -sinx; dst.m22 = cosx;
	}
	
	public static void roll(Matrix4 dst, Angle a) {

		checkNotNull(dst);
		double cosx = cos(-a.getRadians());
		double sinx = sin(-a.getRadians());
		setIdentity(dst);

		dst.m00 = cosx; dst.m10 = -sinx;
		dst.m01 = sinx; dst.m11 =  cosx; 
	}
}
