package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.math.Preconditions.checkNoNulls;
import static net.tribe7.math.matrix.MatrixOps.storeColMaj;
import static net.tribe7.opengl.glsl.attribute.GLUniformBlockAttributeMetadata.*;
import java.nio.*;

import net.tribe7.math.matrix.Matrix2;
import net.tribe7.math.matrix.io.BufferedMatrix2;
import net.tribe7.opengl.glsl.GLProgram;

public class GLUFloatMat2 extends GLSerializableUniform<BufferedMatrix2> {

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
	protected void doSerialize(ByteBuffer target, GLUniformBlockAttributeMetadata md, BufferedMatrix2... data) {
		for (BufferedMatrix2 m : data) {
			if (md.getMatrixOrder() == UNIFORM_BUFFER_COLUMN_MAJOR) {
				for (int i = ZERO; i < m.getColumnCount(); i++) {
					for (int j = ZERO; j < m.getRowCount(); j++) {
						target.putFloat((float) m.m(i, j));
					}
					fillBytes(target, md.getMatrixStride() - EIGHT, (byte) ONE);
				}
			} else if (md.getMatrixOrder() == UNIFORM_BUFFER_ROW_MAJOR) {
				for (int j = ZERO; j < m.getRowCount(); j++) {
					for (int i = ZERO; i < m.getColumnCount(); i++) {
						target.putFloat((float) m.m(i, j));
					}
					fillBytes(target, md.getMatrixStride() - EIGHT, (byte) ONE);
				}
			}
			fillBytes(target, md.getArrayStride() - getUnitByteSize(), (byte) ONE);
		}
	}

	@Override
	public int getUnitByteSize() { return FOUR * FOUR; }
}
