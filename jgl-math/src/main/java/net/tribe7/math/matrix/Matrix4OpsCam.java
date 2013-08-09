package net.tribe7.math.matrix;

import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.math.Preconditions.*;

import static java.lang.Math.*;
import static net.tribe7.math.matrix.MatrixOps.*;
import static net.tribe7.math.vector.VectorOps.*;
import static net.tribe7.math.vector.Vector3Ops.*;

import net.tribe7.math.angle.Angle;
import net.tribe7.math.vector.Vector3;

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

		dst.m(0,0, x.x); dst.m(1,0, x.y); dst.m(2,0, x.z); dst.m(3,0, -dot(eye, x));
		dst.m(0,1, y.x); dst.m(1,1, y.y); dst.m(2,1, y.z); dst.m(3,1, -dot(eye, y));
		dst.m(0,2, z.x); dst.m(1,2, z.y); dst.m(2,2, z.z); dst.m(3,2, -dot(eye, z));
	}

	public static void orbit(Matrix4 dst, Vector3 target, double radius, Angle azimuth, Angle elevation) {

		checkNoNulls(dst, target);

		Vector3 z = new Vector3(
				elevation.cos() * azimuth.cos(), 
				elevation.sin(), 
				elevation.cos() * -azimuth.sin());

		Vector3 x = new Vector3(-azimuth.sin(), 0, -azimuth.cos());
		Vector3 y = new Vector3();

		cross(z, x, y);
		setIdentity(dst);

		dst.m(0,0, x.x); dst.m(1,0, x.y); dst.m(2,0, x.z); dst.m(3,0, dot(x, z) * -radius - dot(x, target));
		dst.m(0,1, y.x); dst.m(1,1, y.y); dst.m(2,1, y.z); dst.m(3,1, dot(y, z) * -radius - dot(y, target));
		dst.m(0,2, z.x); dst.m(1,2, z.y); dst.m(2,2, z.z); dst.m(3,2, dot(z, z) * -radius - dot(z, target));
	}

	public static void pitch(Matrix4 dst, Angle a) {

		checkNotNull(dst);
		double cosx = cos(-a.getRadians());
		double sinx = sin(-a.getRadians());
		setIdentity(dst);

		dst.m(2,1, cosx); dst.m(3,1, -sinx);
		dst.m(2,2, sinx); dst.m(3,2,  cosx);
	}

	public static void yaw(Matrix4 dst, Angle a) {

		checkNotNull(dst);
		double cosx = cos(-a.getRadians());
		double sinx = sin(-a.getRadians());
		setIdentity(dst);

		dst.m(0,0,  cosx); dst.m(2,0, sinx);
		dst.m(0,2, -sinx); dst.m(2,2, cosx);
	}

	public static void roll(Matrix4 dst, Angle a) {

		checkNotNull(dst);
		double cosx = cos(-a.getRadians());
		double sinx = sin(-a.getRadians());
		setIdentity(dst);

		dst.m(0,0, cosx); dst.m(1,0, -sinx);
		dst.m(0,1, sinx); dst.m(1,1,  cosx); 
	}
}
