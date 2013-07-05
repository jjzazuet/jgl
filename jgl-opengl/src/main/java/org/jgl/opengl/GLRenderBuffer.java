package org.jgl.opengl;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.util.GLBufferUtils.*;
import java.nio.IntBuffer;

public class GLRenderBuffer extends GLContextBoundResource {

	private GLImageMetadata bufferFormat = new GLImageMetadata();
	
	@Override
	protected void doInit() {
		IntBuffer ib = IntBuffer.allocate(ONE);
		getGl().glGenRenderbuffers(ONE, ib);
		setGlResourceHandle(ib.get());
	}

	@Override
	protected void doBind() {
		getGl().glBindRenderbuffer(GL_RENDERBUFFER, getGlResourceHandle());
	}

	@Override
	protected void doUnbind() {
		getGl().glBindRenderbuffer(GL_RENDERBUFFER, ZERO);
	}

	@Override
	protected void doDestroy() {
		getGl().glDeleteRenderbuffers(ONE, intBuffer(getGlResourceHandle()));
	}

	public GLImageMetadata getBufferFormat() { return bufferFormat; }
}
