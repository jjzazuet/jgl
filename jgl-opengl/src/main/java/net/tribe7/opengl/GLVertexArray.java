package net.tribe7.opengl;

import static net.tribe7.opengl.util.GLBufferUtils.*;
import static net.tribe7.math.Preconditions.*;
import java.nio.IntBuffer;

public class GLVertexArray extends GLContextBoundResource {
	
	@Override
	protected int doInit() {
		IntBuffer b = IntBuffer.allocate(ONE);
		getGl().glGenVertexArrays(ONE, b);
		return b.get();
	}

	@Override
	protected void doBind() {
		getGl().glBindVertexArray(getGlResourceHandle());
	}

	@Override
	protected void doUnbind() { getGl().glBindVertexArray(ZERO); }

	@Override
	protected void doDestroy() {
		getGl().glDeleteVertexArrays(ONE, intBuffer(getGlResourceHandle()));
	}
}