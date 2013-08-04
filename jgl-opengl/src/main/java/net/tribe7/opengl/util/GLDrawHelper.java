package net.tribe7.opengl.util;

import static net.tribe7.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import net.tribe7.geom.*;
import net.tribe7.opengl.*;

public class GLDrawHelper extends GLContextBoundResource {

	public void glIndexedDraw(int glMode, GLBuffer buffer) {
		checkNotNull(buffer);
		buffer.getRawBuffer().clear();
		buffer.getGl().glDrawElements(glMode, 
				buffer.getRawBuffer().capacity(), 
				buffer.getBufferMetadata().getGlPrimitiveType(), 
				buffer.getRawBuffer());
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

	@Override
	protected void doInit() {}
	@Override
	protected void doBind() { throw new UnsupportedOperationException(); }
	@Override
	protected void doUnbind() { throw new UnsupportedOperationException(); }
	@Override
	protected void doDestroy() { throw new UnsupportedOperationException(); }
}
