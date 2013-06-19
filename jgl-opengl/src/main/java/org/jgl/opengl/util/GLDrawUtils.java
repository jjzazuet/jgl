package org.jgl.opengl.util;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import org.jgl.opengl.GLBuffer;

public class GLDrawUtils {

	public static void glIndexedDraw(int glMode, GL gl, GLBuffer buffer) {
		checkNotNull(gl);
		checkNotNull(buffer);
		buffer.getRawBuffer().clear();
		gl.glDrawElements(glMode, 
				buffer.getRawBuffer().capacity(), 
				buffer.getBufferMetadata().getGlPrimitiveType(), 
				buffer.getRawBuffer());
	}
	
	public static void glViewPort(GL3 gl, GLViewSize newViewport) {
		gl.glViewport(newViewport.x, newViewport.y, 
				(int) newViewport.width, (int) newViewport.height);
	}
}
