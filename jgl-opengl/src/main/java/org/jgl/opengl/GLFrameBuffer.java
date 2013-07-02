package org.jgl.opengl;

import static org.jgl.opengl.util.GLBufferUtils.*;
import java.nio.IntBuffer;

public class GLFrameBuffer extends GLContextBoundResource {

	public GLFrameBuffer() {
	}

	@Override
	protected void doInit() {
		IntBuffer ib = IntBuffer.allocate(ONE);
		getGl().glGenFramebuffers(ONE, ib);
		setGlResourceHandle(ib.get());
		checkError();
	}

	@Override
	protected void doBind() {

	}

	@Override
	protected void doUnbind() {

	}

	@Override
	protected void doDestroy() {
		getGl().glDeleteFramebuffers(ONE, intBuffer(getGlResourceHandle()));
	}
}
