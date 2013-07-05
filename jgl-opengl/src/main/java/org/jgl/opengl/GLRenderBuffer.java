package org.jgl.opengl;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.util.GLBufferUtils.*;
import java.nio.IntBuffer;

public class GLRenderBuffer extends GLContextBoundResource {

	private boolean storageReady = false;
	private GLImageMetadata bufferFormat = new GLImageMetadata();

	public void initStorage() {
		bind();
		getGl().glRenderbufferStorage(GL_RENDERBUFFER, 
				getBufferFormat().getInternalFormat(), 
				getBufferFormat().getWidth(), 
				getBufferFormat().getHeight());
		checkError();
		unbind();
	}

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
	public boolean isStorageReady() { return storageReady; }
}