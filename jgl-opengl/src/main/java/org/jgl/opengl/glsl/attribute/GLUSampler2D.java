package org.jgl.opengl.glsl.attribute;

import static com.google.common.base.Preconditions.*;
import org.jgl.opengl.GLTexture2D;
import org.jgl.opengl.glsl.GLProgram;

public class GLUSampler2D extends GLUniformAttribute<GLTexture2D> {

	public GLUSampler2D(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	@Override
	public void doSet(int index, GLTexture2D value) {
		checkElementIndex(index, getSize());
		checkNotNull(value);
		checkNotNull(value.getImage());
		getProgram().checkBound();
		getProgram().getGl().glActiveTexture(value.getTextureUnitEnum());
		getProgram().checkError();
		value.bind();
		getProgram().getGl().glUniform1i(getIndexLocation(index), value.getTextureUnit());
		getProgram().checkError();
		value.unbind();
	}
}
