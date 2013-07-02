package org.jgl.opengl.glsl.attribute;

import static org.jgl.math.Preconditions.checkNoNulls;
import static org.jgl.math.matrix.Matrix2Ops.storeColMaj;

import java.nio.FloatBuffer;

import org.jgl.math.matrix.Matrix2;
import org.jgl.math.matrix.io.BufferedMatrix2;
import org.jgl.opengl.glsl.GLProgram;

public class GLUFloatMat2 extends GLUniformAttribute<BufferedMatrix2> {

	public GLUFloatMat2(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void colMaj(int index, FloatBuffer dst, Matrix2 src) {
		checkNoNulls(dst, src);
		storeColMaj(dst, src);
		getProgram().getGl().glUniformMatrix2fv(getIndexLocation(index), ONE, false, dst);
		getProgram().checkError();
	}

	public void colMaj(FloatBuffer dst, Matrix2 src) {
		colMaj(ZERO, dst, src);
	}

	@Override
	protected void doSet(int index, BufferedMatrix2 value) {
		colMaj(index, value.getBackingBuffer(), value);
	}
}
