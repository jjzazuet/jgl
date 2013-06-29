package org.jgl.opengl.glsl.attribute;

import static org.jgl.math.Preconditions.checkNoNulls;
import static org.jgl.math.matrix.Matrix2Ops.storeColMaj;

import java.nio.FloatBuffer;

import org.jgl.math.matrix.Matrix2;
import org.jgl.opengl.glsl.GLProgram;

public class GLUFloatMat2 extends GLUniformAttribute {

	public GLUFloatMat2(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void setColMaj(int index, FloatBuffer dst, Matrix2 src) {
		checkNoNulls(dst, src);
		storeColMaj(dst, src);
		getProgram().getGl().glUniformMatrix2fv(getIndexLocation(index), ONE, false, dst);
		getProgram().checkError();
	}
	
	public void setColMaj(FloatBuffer dst, Matrix2 src) {
		setColMaj(ZERO, dst, src);
	}
}
