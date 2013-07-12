package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;
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
	protected void doSet(int index, Float value) {
		setArray(index, value);
	}
}