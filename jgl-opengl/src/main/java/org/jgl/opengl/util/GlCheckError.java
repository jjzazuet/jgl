package org.jgl.opengl.util;

import static com.google.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import javax.media.opengl.GL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlCheckError {

	private static final Logger log = LoggerFactory.getLogger(GlCheckError.class);
	
	public void apply(GL gl) {
		if (log.isDebugEnabled()) {
			
			int glError = get(gl);
			
			if (glError != GL_NO_ERROR) {
				throw new IllegalStateException(
						String.format("Invalid OpenGL state: [%s]", glError));
			}
		}		
	}
	
	public int get(GL gl) {
		checkNotNull(gl);
		return gl.glGetError();		
	}
}