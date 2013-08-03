package net.tribe7.math.matrix;

import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.math.Preconditions.*;

import java.nio.FloatBuffer;

public class Matrix4Ops {

	public static void copy(Matrix4 src, Matrix4 dst) {

		checkNoNulls(src, dst);

		dst.m00 = src.m00; dst.m10 = src.m10; dst.m20 = src.m20; dst.m30 = src.m30;
		dst.m01 = src.m01; dst.m11 = src.m11; dst.m21 = src.m21; dst.m31 = src.m31;
		dst.m02 = src.m02; dst.m12 = src.m12; dst.m22 = src.m22; dst.m32 = src.m32;
		dst.m03 = src.m03; dst.m13 = src.m13; dst.m23 = src.m23; dst.m33 = src.m33;
	}

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

		dst.m00 = m00; dst.m10 = m10; dst.m20 = m20; dst.m30 = m30;
		dst.m01 = m01; dst.m11 = m11; dst.m21 = m21; dst.m31 = m31;
		dst.m02 = m02; dst.m12 = m12; dst.m22 = m22; dst.m32 = m32;
		dst.m03 = m03; dst.m13 = m13; dst.m23 = m23; dst.m33 = m33;
	}

	public static void setIdentity(Matrix4 m) {
		checkNotNull(m);
		m.m00 = 1; m.m10 = 0; m.m20 = 0; m.m30 = 0;
		m.m01 = 0; m.m11 = 1; m.m21 = 0; m.m31 = 0;
		m.m02 = 0; m.m12 = 0; m.m22 = 1; m.m32 = 0;
		m.m03 = 0; m.m13 = 0; m.m23 = 0; m.m33 = 1;
	}

	public static void setZero(Matrix4 m) {
		checkNotNull(m);
		m.m00 = 0; m.m10 = 0; m.m20 = 0; m.m30 = 0;
		m.m01 = 0; m.m11 = 0; m.m21 = 0; m.m31 = 0;
		m.m02 = 0; m.m12 = 0; m.m22 = 0; m.m32 = 0;
		m.m03 = 0; m.m13 = 0; m.m23 = 0; m.m33 = 0;
	}

	public static void storeColMaj(FloatBuffer b, Matrix4 ... src) {

		checkNoNulls(src);
		checkNotNull(b);
		checkArgument(src.length >= 1);
		checkArgument(b.capacity() >= src.length * Matrix4.COMPONENT_SIZE);

		b.clear();

		for (Matrix4 m : src) {
			checkState(b.remaining() >= Matrix4.COMPONENT_SIZE);
			b.put((float) m.m00); b.put((float) m.m01); b.put((float) m.m02); b.put((float) m.m03);
			b.put((float) m.m10); b.put((float) m.m11); b.put((float) m.m12); b.put((float) m.m13);
			b.put((float) m.m20); b.put((float) m.m21); b.put((float) m.m22); b.put((float) m.m23);
			b.put((float) m.m30); b.put((float) m.m31); b.put((float) m.m32); b.put((float) m.m33);
		}

		b.flip();
	}
}
