package org.jgl.opengl.util;

import static javax.media.opengl.GL.*;
import static com.google.common.base.Preconditions.checkNotNull;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import org.jgl.geom.FaceWinding;
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
