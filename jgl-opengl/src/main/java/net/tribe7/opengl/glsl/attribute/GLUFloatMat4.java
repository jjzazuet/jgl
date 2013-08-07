package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.math.Preconditions.checkNoNulls;
import static net.tribe7.math.matrix.Matrix4Ops.storeColMaj;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.tribe7.math.matrix.ColumnMajorOrder;
import net.tribe7.math.matrix.Matrix4;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.opengl.glsl.GLProgram;

public class GLUFloatMat4 extends GLUniformAttribute<BufferedMatrix4> {

	public GLUFloatMat4(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void colMaj(int index, FloatBuffer dst, Matrix4 src) {
		checkNoNulls(dst, src);
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
	protected void doSerialize(ByteBuffer target, GLUniformBlockAttributeMetadata md, BufferedMatrix4 ... data) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getUnitByteSize() {
		return (FOUR * FOUR) * FOUR;
	}
}
