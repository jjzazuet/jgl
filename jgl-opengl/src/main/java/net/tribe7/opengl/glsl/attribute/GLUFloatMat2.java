package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.math.Preconditions.checkNoNulls;
import static net.tribe7.math.matrix.Matrix2Ops.storeColMaj;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.tribe7.math.matrix.Matrix2;
import net.tribe7.math.matrix.io.BufferedMatrix2;
import net.tribe7.opengl.glsl.GLProgram;

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

	@Override
	protected void doSerialize(ByteBuffer target, GLUniformBlockAttributeMetadata md, BufferedMatrix2 ... data) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getUnitByteSize() {
		return (FOUR * TWO) * TWO;
	}
}
