package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;
import java.nio.Buffer;
import java.nio.IntBuffer;

public class GLBuffer extends GLContextBoundResource {

	protected final int glUsageHint;
	protected final int glBufferType;
	
	protected Buffer rawBuffer;
	protected final GLBufferMetadata bufferMetadata;
	
	protected GLBuffer(int glBufferType, int glUsageHint, GLBufferMetadata componentMetadata) {
		this.glBufferType = glBufferType;
		this.glUsageHint = glUsageHint;
		this.bufferMetadata = checkNotNull(componentMetadata);
	}
	
	@Override
	protected void doInit() { 
		IntBuffer b = IntBuffer.allocate(1);
		getGl().glGenBuffers(1, b);
		setGlResourceHandle(b.get());
	}

	@Override
	protected void doBind() { 
		getGl().glBindBuffer(getGlBufferType(), getGlResourceHandle()); 
	}
	
	@Override
	protected void doUnbind() { 
		getGl().glBindBuffer(getGlBufferType(), 0);
	}
	
	@Override
	protected void doDestroy() { 
		IntBuffer b = IntBuffer.wrap(new int [] {getGlResourceHandle()});
		b.flip();
		getGl().glDeleteBuffers(1, b);
	}

	public int getGlBufferType() { return glBufferType; }
	public GLBufferMetadata getBufferMetadata() { return bufferMetadata; }
	public Buffer getRawBuffer() { return rawBuffer; }
	public void setRawBuffer(Buffer rawBuffer) { this.rawBuffer = checkNotNull(rawBuffer); }
}