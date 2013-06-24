package org.jgl.opengl.util;

import static com.google.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2GL3.GL_PRIMITIVE_RESTART;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import org.jgl.geom.FaceWinding;
import org.jgl.opengl.GLBuffer;

public class GLDrawUtils {

	public static final int MINNUS_ONE = -1;
	
	public static void glIndexedDraw(int glMode, GL gl, GLBuffer buffer) {
		checkNotNull(gl);
		checkNotNull(buffer);
		buffer.getRawBuffer().clear();

		gl.glDrawElements(glMode, 
				buffer.getRawBuffer().capacity(), 
				buffer.getBufferMetadata().getGlPrimitiveType(), 
				buffer.getRawBuffer());
	}

	public static void glIndexedDraw(int glMode, GL3 gl, GLBuffer buffer, int restartIndex) {
		checkArgument(restartIndex >= 0);
		gl.glEnable(GL_PRIMITIVE_RESTART);
		gl.glPrimitiveRestartIndex(restartIndex);
		glIndexedDraw(glMode, gl, buffer);
		gl.glDisable(GL_PRIMITIVE_RESTART);
	}
	
	public static void glViewPort(GL3 gl, GLViewSize newViewport) {
		checkNotNull(gl);
		checkNotNull(newViewport);
		gl.glViewport(newViewport.x, newViewport.y, 
				(int) newViewport.width, (int) newViewport.height);
	}
	
	public static int getGlFrontFace(FaceWinding fw) {
		checkNotNull(fw);
		switch (fw) {
			case CW: return GL_CW;
			case CCW: return GL_CCW;
			default: throw new IllegalStateException(fw.toString());
		}
	}
	
	public static void glFrontFace(GL gl, FaceWinding fw) {
		checkNotNull(gl);
		gl.glFrontFace(getGlFrontFace(fw));
	}
}
