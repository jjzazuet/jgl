package net.tribe7.opengl;

import static net.tribe7.opengl.GLConstants.GL_BUFFER_USAGE_HINT;
import static net.tribe7.opengl.util.GLBufferUtils.*;
import static net.tribe7.common.base.Preconditions.*;
import java.nio.*;

public class GLBuffer extends GLContextBoundResource {

	protected final int glUsageHint;
	protected final int glBufferType;

	protected Buffer rawBuffer;
	protected final GLBufferMetadata bufferMetadata;
	
	protected GLBuffer(int glBufferType, int glUsageHint, GLBufferMetadata componentMetadata) {
		checkArgument(GL_BUFFER_USAGE_HINT.contains(glUsageHint));
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