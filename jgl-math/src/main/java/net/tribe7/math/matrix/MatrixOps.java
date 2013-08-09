package net.tribe7.math.matrix;

import static net.tribe7.math.Preconditions.*;
import static net.tribe7.common.base.Preconditions.*;

import java.nio.FloatBuffer;

public class MatrixOps {

	public static final void copy(Matrix src, Matrix dst) {
		checkNoNulls(src, dst);
		dst.checkDimensions(src);
		for (int i = ZERO; i < src.getColumnCount(); i++) {
			for (int j = ZERO; j < src.getRowCount(); j++) {
				dst.m(i, j, src.m(i, j));
			}
		}
	}

	/**
	 * Assumes all matrices are square and of the same dimensions.
	 * @param l
	 * @param r
	 * @param dst
	 */
	public static final void mul(Matrix l, Matrix r, Matrix dst) {

		checkNoNulls(l, r, dst);
		checkArgument(l.isSquare());
		l.checkDimensions(r).checkDimensions(dst);

		int i, j, k;
		double ax;

		for (i = ZERO; i < l.getColumnCount(); i++) {
			for (j = ZERO; j < l.getColumnCount(); j++) {
				ax = ZERO;
				for (k = ZERO; k < l.getColumnCount(); k++) {
					ax = ax + (l.m(k ,j) * r.m(i, k));
				}
				dst.m(i, j, ax);
			}
		}
	}

	public static final void setIdentity(Matrix dst) {
		checkNotNull(dst);
		checkArgument(dst.isSquare());
		for (int i = ZERO; i < dst.getColumnCount(); i++) {
			for (int j = ZERO; j < dst.getRowCount(); j++) {
				if (i == j) {
					dst.m(i, j, ONE);
				}
			}
		}
	}

	public static final void setVal(Matrix dst, double val) {
		checkNotNull(dst);
		for (int i = ZERO; i < dst.getColumnCount(); i++) {
			for (int j = ZERO; j < dst.getRowCount(); j++) {
				dst.m(i, j, val);
			}
		}
	}

	public static final void setZero(Matrix dst) {
		setVal(dst, ZERO);
	}

	public static final void storeColMaj(FloatBuffer b, Matrix ... values) {

		checkNotNull(b);
		checkNoNulls(values);
		checkArgument(values.length >= ONE);

		int totalComponentSize = 0;

		for (Matrix m : values) {
			totalComponentSize += m.getComponentSize();
		}

		checkArgument(b.remaining() >= totalComponentSize);
		b.clear();

		for (Matrix m : values) {
			for (int i = 0; i < m.getColumnCount(); i++) {
				for (int j = 0; j < m.getRowCount(); j++) {
					b.put((float) m.m(i, j));
				}
			}
		}

		b.flip();
	}
	
}
