package org.jgl.math.matrix;

import static com.google.common.base.Preconditions.*;
import static org.jgl.math.Preconditions.*;

import java.nio.FloatBuffer;

public class Matrix4Ops {

	public static void mul(Matrix4 l, Matrix4 r, Matrix4 dst) {
		
		checkNoNulls(l, r, dst);
		
		double m00 = l.m00 * r.m00 + l.m10 * r.m01 + l.m20 * r.m02 + l.m30 * r.m03;
		double m01 = l.m01 * r.m00 + l.m11 * r.m01 + l.m21 * r.m02 + l.m31 * r.m03;
		double m02 = l.m02 * r.m00 + l.m12 * r.m01 + l.m22 * r.m02 + l.m32 * r.m03;
		double m03 = l.m03 * r.m00 + l.m13 * r.m01 + l.m23 * r.m02 + l.m33 * r.m03;
		double m10 = l.m00 * r.m10 + l.m10 * r.m11 + l.m20 * r.m12 + l.m30 * r.m13;
		double m11 = l.m01 * r.m10 + l.m11 * r.m11 + l.m21 * r.m12 + l.m31 * r.m13;
		double m12 = l.m02 * r.m10 + l.m12 * r.m11 + l.m22 * r.m12 + l.m32 * r.m13;
		double m13 = l.m03 * r.m10 + l.m13 * r.m11 + l.m23 * r.m12 + l.m33 * r.m13;
		double m20 = l.m00 * r.m20 + l.m10 * r.m21 + l.m20 * r.m22 + l.m30 * r.m23;
		double m21 = l.m01 * r.m20 + l.m11 * r.m21 + l.m21 * r.m22 + l.m31 * r.m23;
		double m22 = l.m02 * r.m20 + l.m12 * r.m21 + l.m22 * r.m22 + l.m32 * r.m23;
		double m23 = l.m03 * r.m20 + l.m13 * r.m21 + l.m23 * r.m22 + l.m33 * r.m23;
		double m30 = l.m00 * r.m30 + l.m10 * r.m31 + l.m20 * r.m32 + l.m30 * r.m33;
		double m31 = l.m01 * r.m30 + l.m11 * r.m31 + l.m21 * r.m32 + l.m31 * r.m33;
		double m32 = l.m02 * r.m30 + l.m12 * r.m31 + l.m22 * r.m32 + l.m32 * r.m33;
		double m33 = l.m03 * r.m30 + l.m13 * r.m31 + l.m23 * r.m32 + l.m33 * r.m33;

		dst.m00 = m00; dst.m01 = m01; dst.m02 = m02; dst.m03 = m03;
		dst.m10 = m10; dst.m11 = m11; dst.m12 = m12; dst.m13 = m13;
		dst.m20 = m20; dst.m21 = m21; dst.m22 = m22; dst.m23 = m23;
		dst.m30 = m30; dst.m31 = m31; dst.m32 = m32; dst.m33 = m33;
	}
	
	public static void setIdentity(Matrix4 m) {
		checkNotNull(m);
		m.m00 = 1; m.m01 = 0; m.m02 = 0; m.m03 = 0;
		m.m10 = 0; m.m11 = 1; m.m12 = 0; m.m13 = 0;
		m.m20 = 0; m.m21 = 0; m.m22 = 1; m.m23 = 0;
		m.m30 = 0; m.m31 = 0; m.m32 = 0; m.m33 = 1;
	}
	
	public static void setZero(Matrix4 m) {
		checkNotNull(m);
		m.m00 = 0; m.m01 = 0; m.m02 = 0; m.m03 = 0; 
		m.m10 = 0; m.m11 = 0; m.m12 = 0; m.m13 = 0;
		m.m20 = 0; m.m21 = 0; m.m22 = 0; m.m23 = 0;
		m.m30 = 0; m.m31 = 0; m.m32 = 0; m.m33 = 0;
	}
	
	public static void store(Matrix4 src, FloatBuffer b) {
		checkNoNulls(src, b);
		b.clear();
		b.put((float) src.m00); b.put((float) src.m01); b.put((float) src.m02); b.put((float) src.m03);
		b.put((float) src.m10); b.put((float) src.m11); b.put((float) src.m12); b.put((float) src.m13);
		b.put((float) src.m20); b.put((float) src.m21); b.put((float) src.m22); b.put((float) src.m23);
		b.put((float) src.m30); b.put((float) src.m31); b.put((float) src.m32); b.put((float) src.m33);
		b.flip();
	}
}