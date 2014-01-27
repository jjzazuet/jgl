package net.tribe7.opengl.util;

import static net.tribe7.math.Preconditions.*;
import static net.tribe7.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;

import sun.nio.ch.DirectBuffer; // TODO this may not run on Linux :(
import net.tribe7.geom.*;
import net.tribe7.opengl.*;

public class GLDrawHelper extends GLContextResource {

	public void glIndexedDraw(int glMode, GLBuffer buffer) {
		checkNotNull(buffer);
		checkArgument(buffer.getRawBuffer().isDirect(), "Non direct buffer: %s", buffer);
		DirectBuffer db = (DirectBuffer) buffer.getRawBuffer();
		buffer.getRawBuffer().clear();
		buffer.getGl().glDrawElements(glMode, 
				buffer.getRawBuffer().capacity(), 
				buffer.getBufferMetadata().getGlPrimitiveType(),
				db.address());
		buffer.checkError();
	}

	public void glIndexedDraw(int glMode, GLBuffer buffer, int restartIndex) {
		checkArgument(restartIndex >= ZERO);
		getGl().glEnable(GL_PRIMITIVE_RESTART);
		getGl().glPrimitiveRestartIndex(restartIndex);
		glIndexedDraw(glMode, buffer);
		getGl().glDisable(GL_PRIMITIVE_RESTART);
	}

	public void glViewPort(GLViewSize newViewport) {
		checkNotNull(newViewport);
		getGl().glViewport(newViewport.x, newViewport.y, 
				(int) newViewport.width, (int) newViewport.height);
	}

	public void glViewPort(int width, int height) {
		getGl().glViewport(ZERO, ZERO, width, height);
	}

	public static int getGlFrontFace(FaceWinding fw) {
		checkNotNull(fw);
		switch (fw) {
			case CW: return GL_CW;
			case CCW: return GL_CCW;
			default: throw new IllegalStateException(fw.toString());
		}
	}
	
	public void glFrontFace(FaceWinding fw) {
		getGl().glFrontFace(getGlFrontFace(fw));
		checkError();
	}
	
	public GLDrawHelper glClearColor() {
		getGl().glClear(GL_COLOR_BUFFER_BIT);
		checkError();
		return this;
	}

	public GLDrawHelper glClearDepth() {
		getGl().glClear(GL_DEPTH_BUFFER_BIT);
		checkError();
		return this;
	}

	public GLDrawHelper glClearStencil() {
		getGl().glClear(GL_STENCIL_BUFFER_BIT);
		checkError();
		return this;
	}
}
