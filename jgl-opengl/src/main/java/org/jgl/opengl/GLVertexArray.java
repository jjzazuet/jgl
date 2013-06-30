package org.jgl.opengl;

import static org.jgl.opengl.util.GLBufferUtils.*;
import java.nio.IntBuffer;

public class GLVertexArray extends GLContextBoundResource {
	
	@Override
	protected void doInit() {
		IntBuffer b = IntBuffer.allocate(1);
		getGl().glGenVertexArrays(1, b);
		setGlResourceHandle(b.get());
	}

	@Override
	protected void doBind() {
		getGl().glBindVertexArray(getGlResourceHandle());
	}

	@Override
	protected void doUnbind() { getGl().glBindVertexArray(ZERO); }

	@Override
	protected void doDestroy() {
		getGl().glDeleteVertexArrays(1, intBuffer(getGlResourceHandle()));
	}
}