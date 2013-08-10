package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;

import java.nio.ByteBuffer;

import net.tribe7.opengl.glsl.GLProgram;

public class GLUFloat extends GLUniformAttribute<Float> {

	public GLUFloat(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void setArray(int index, float ... value) {
		checkNotNull(value);
		int offset = index + (value.length - 1);
		checkElementIndex(offset, getSize());
		getProgram().checkBound();
		getProgram().getGl().glUniform1fv(getIndexLocation(index), value.length, bufferData(value));
		getProgram().checkError();
	}

	@Override
	protected void doSet(int index, Float value) { setArray(index, value); }

	@Override
	protected void doSerialize(ByteBuffer target, GLUniformBlockAttributeMetadata md, Float... data) {

		int elementsSerialized = 0;

		for(Float f : data) {
			target.putFloat(f);
			elementsSerialized++;
			if (elementsSerialized < getSize()) {
				fillBytes(target, md.getArrayStride(), (byte) ONE);
			}
		}
	}

	@Override
	public int getUnitByteSize() { return FOUR; }
}
