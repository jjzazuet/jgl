package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;
import net.tribe7.opengl.glsl.GLProgram;

public class GLUInt extends GLUniformAttribute<Integer> {

	public GLUInt(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void setArray(int index, int ... value) {
		checkNotNull(value);
		int offset = index + (value.length - 1);
		checkElementIndex(offset, getSize());
		getProgram().checkBound();
		getProgram().getGl().glUniform1iv(getIndexLocation(index), value.length, bufferData(value));
		getProgram().checkError();
	}

	@Override
	public void doSet(int index, Integer value) {
		setArray(index, value);
	}
}
