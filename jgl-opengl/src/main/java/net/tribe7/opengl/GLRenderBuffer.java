package net.tribe7.opengl;

import static javax.media.opengl.GL.*;
import static net.tribe7.opengl.util.GLBufferUtils.*;
import static net.tribe7.math.Preconditions.*;
import java.nio.IntBuffer;

public class GLRenderBuffer extends GLContextBoundResource {

	private boolean storageReady = false;
	private GLImageMetadata bufferFormat = new GLImageMetadata();

	public void initStorage() {
		checkBound();
		getGl().glRenderbufferStorage(GL_RENDERBUFFER, 
				getBufferFormat().getInternalFormat(), 
				getBufferFormat().getWidth(), 
				getBufferFormat().getHeight());
		checkError();
		storageReady = true;
	}

	@Override
	protected int doInit() {
		IntBuffer ib = IntBuffer.allocate(ONE);
		getGl().glGenRenderbuffers(ONE, ib);
		return ib.get();
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