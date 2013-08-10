package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.opengl.glsl.attribute.GLUniformBlockAttributeMetadata.*;
import static net.tribe7.math.matrix.MatrixOps.storeColMaj;

import java.nio.*;

import net.tribe7.math.matrix.*;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.opengl.glsl.GLProgram;

public class GLUFloatMat4 extends GLUniformAttribute<BufferedMatrix4> {

	public GLUFloatMat4(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void colMaj(int index, FloatBuffer dst, Matrix4 src) {
		storeColMaj(dst, src);
		getProgram().getGl().glUniformMatrix4fv(getIndexLocation(index), ONE, false, dst);
		getProgram().checkError();
	}

	public void colMaj(int index, BufferedMatrix4 src) {
		colMaj(index, src.getBackingBuffer(), src);
	}

	@Override
	@ColumnMajorOrder
	public void doSet(int index, BufferedMatrix4 value) {
		colMaj(index, value);
	}

	@Override
	protected void doSerialize(ByteBuffer target, GLUniformBlockAttributeMetadata md, BufferedMatrix4... data) {

		int elementsSerialized = 0;

		for (BufferedMatrix4 m : data) {
			if (md.getMatrixOrder() == UNIFORM_BUFFER_COLUMN_MAJOR) {
				for (int i = ZERO; i < m.getColumnCount(); i++) {
					for (int j = ZERO; j < m.getRowCount(); j++) {
						target.putFloat((float) m.m(i, j));
					}
					if (i+1 == m.getColumnCount()) { 
						elementsSerialized++; 
					}
					if (elementsSerialized < getSize()) {
						fillBytes(target, md.getMatrixStride(), (byte) ONE);
					}
				}
			} else if (md.getMatrixOrder() == UNIFORM_BUFFER_ROW_MAJOR) {
				for (int j = ZERO; j < m.getRowCount(); j++) {
					for (int i = ZERO; i < m.getColumnCount(); i++) {
						target.putFloat((float) m.m(i, j));
					}
					if (j+1 == m.getRowCount()) { 
						elementsSerialized++; 
					}
					if (elementsSerialized < getSize()) {
						fillBytes(target, md.getMatrixStride(), (byte) ONE);
					}
				}
			}
			for (int i = ZERO; i < md.getArrayStride(); i++) {
				target.put((byte) ZERO);
			}
		}
	}

	@Override
	public int getUnitByteSize() { return SIXTEEN * FOUR; }
}
