package org.jgl.opengl.util;

import static com.google.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import javax.media.opengl.GL;

public class GlCheckError {

	public void apply(GL gl) {
		
		checkNotNull(gl);
		
		int glError = gl.glGetError();
		
		if (glError != GL_NO_ERROR) {
			throw new IllegalStateException(
					String.format("Invalid OpenGL state: [%s]", glError));
		}
	}

}
