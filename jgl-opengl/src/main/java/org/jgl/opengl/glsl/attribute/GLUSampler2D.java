package org.jgl.opengl.glsl.attribute;

import static com.google.common.base.Preconditions.*;
import org.jgl.opengl.GLTexture2D;
import org.jgl.opengl.glsl.GLProgram;

public class GLUSampler2D extends GLUniformAttribute {

	public GLUSampler2D(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void set(int index, GLTexture2D target) {
		checkElementIndex(index, getSize());
		checkNotNull(target);
		checkArgument(target.isInitialized());
		checkArgument(target.isBound());
		checkNotNull(target.getImage());
		getProgram().checkBound();
		getProgram().getGl().glUniform1i(getIndexLocation(index), target.getTextureUnit());
		getProgram().checkError();
		
	}

	public void set(GLTexture2D target) {
		set(ZERO, target);
	}
}
