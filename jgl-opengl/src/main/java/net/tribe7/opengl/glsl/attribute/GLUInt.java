package net.tribe7.opengl.glsl.attribute;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.common.base.Preconditions.*;

import java.nio.ByteBuffer;
import net.tribe7.opengl.glsl.GLProgram;

public class GLUInt extends GLSerializableUniform<Integer> {

	public GLUInt(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void setArray(int index, int ... value) {
		checkNotNull(value);
		int offset = index + (value.length - 1);
		checkElementIndex(offset, getSize());
		getProgram().checkBound();

		switch (getGlType()) {
		case GL_UNSIGNED_INT:
			getProgram().getGl().glUniform1uiv(getIndexLocation(index), value.length, bufferData(value));
			break;
		case GL_INT:
			getProgram().getGl().glUniform1iv(getIndexLocation(index), value.length, bufferData(value));
			break;
		default: throw new IllegalArgumentException(Integer.toHexString(getGlType()));
		}
		getProgram().checkError();
	}

	@Override
	public void doSet(int index, Integer value) {
		setArray(index, value);
	}

	@Override
	protected void doSerialize(ByteBuffer target, GLUniformBlockAttributeMetadata md, Integer... data) {
		for (Integer i : data) {
			target.putInt(i);
			fillBytes(target, md.getArrayStride() - getUnitByteSize(), (byte) ONE);
		}
	}

	@Override
	public int getUnitByteSize() { return FOUR; }
}
