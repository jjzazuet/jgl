package org.jgl.math.matrix;

import static com.google.common.base.Preconditions.*;
import static org.jgl.math.Preconditions.*;

import static java.lang.Math.*;
import static org.jgl.math.matrix.Matrix4Ops.*;
import static org.jgl.math.vector.VectorOps.*;
import static org.jgl.math.vector.Vector3Ops.*;

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
		
		dst.m00 = x.x; dst.m01 = x.y; dst.m02 = x.z; dst.m03 = -dot(eye, x);
		dst.m10 = y.x; dst.m11 = y.y; dst.m12 = y.z; dst.m13 = -dot(eye, y);
		dst.m20 = z.x; dst.m21 = z.y; dst.m22 = z.z; dst.m23 = -dot(eye, z);
	}
	
	public static void orbit(Matrix4 dst, Vector3 target, 
			double radius, double azimuthDeg, double elevationDeg) {
		
		checkNoNulls(dst, target);
		
		azimuthDeg = toRadians(azimuthDeg);
		elevationDeg = toRadians(elevationDeg);
		
		Vector3 z = new Vector3(
				cos(elevationDeg) * cos(azimuthDeg), 
				sin(elevationDeg), 
				cos(elevationDeg) * -sin(azimuthDeg));
		
		Vector3 x = new Vector3(-sin(azimuthDeg), 0, -cos(azimuthDeg));
		Vector3 y = new Vector3();
		
		cross(z, x, y);
		setIdentity(dst);
		
		dst.m00 = x.x; dst.m01 = x.y; dst.m02 = x.z; dst.m03 = dot(x, z) * -radius - dot(x, target);
		dst.m10 = y.x; dst.m11 = y.y; dst.m12 = y.z; dst.m13 = dot(y, z) * -radius - dot(y, target);
		dst.m20 = z.x; dst.m21 = z.y; dst.m22 = z.z; dst.m23 = dot(z, z) * -radius - dot(z, target);
	}
	
	public static void pitch(Matrix4 dst, double angDeg) {
		
		checkNotNull(dst);	
		angDeg = toRadians(angDeg);
		double angCos = cos(-angDeg);
		double angSin = sin(-angDeg);
		setIdentity(dst);
		
		dst.m11 = angCos; dst.m12 = -angSin;
		dst.m21 = angSin; dst.m22 =  angCos;
	}
	
	public static void yaw(Matrix4 dst, double angDeg) {
		
		checkNotNull(dst);
		angDeg = toRadians(angDeg);
		double angCos = cos(-angDeg);
		double angSin = sin(-angDeg);
		setIdentity(dst);

		dst.m00 =  angCos; dst.m02 = angSin;
		dst.m20 = -angSin; dst.m22 = angCos;
	}
	
	public static void roll(Matrix4 dst, double angDeg) {

		checkNotNull(dst);
		angDeg = toRadians(angDeg);
		double angCos = cos(-angDeg);
		double angSin = sin(-angDeg);
		setIdentity(dst);

		dst.m00 = angCos; dst.m01 = -angSin;
		dst.m10 = angSin; dst.m11 =  angCos;
	}
}
