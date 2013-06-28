package org.jgl.opengl.glsl.attribute;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.jgl.opengl.glsl.GLProgram;

public class GLUFloat extends GLUniformAttribute {

	public GLUFloat(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void set(float value) { set(ZERO, value); }

	public void set(int index, float ... value) {
		checkNotNull(value);
		checkArgument(value.length <= getSize());
		getProgram().checkBound();
		getProgram().getGl().glUniform1iv(getIndexLocation(index), value.length, bufferData(value));
		getProgram().checkError();
	}

}
