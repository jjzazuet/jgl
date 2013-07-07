package org.jgl.opengl;

import static org.jgl.opengl.util.GLBufferUtils.*;
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
		IntBuffer b = IntBuffer.allocate(ONE);
		getGl().glGenBuffers(ONE, b);
		setGlResourceHandle(b.get());
	}

	@Override
	protected void doBind() { 
		getGl().glBindBuffer(getGlBufferType(), getGlResourceHandle()); 
	}

	@Override
	protected void doUnbind() { 
		getGl().glBindBuffer(getGlBufferType(), ZERO);
	}

	@Override
	protected void doDestroy() { 
		getGl().glDeleteBuffers(ONE, intBuffer(getGlResourceHandle()));
	}

	public int getGlBufferType() { return glBufferType; }
	public GLBufferMetadata getBufferMetadata() { return bufferMetadata; }
	public Buffer getRawBuffer() { return rawBuffer; }
	public void setRawBuffer(Buffer rawBuffer) { this.rawBuffer = checkNotNull(rawBuffer); }
}