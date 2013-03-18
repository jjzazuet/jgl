package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;
import java.nio.IntBuffer;
import java.util.*;

public class GLVertexArray extends GLContextBoundResource {

	private Set<GLVertexAttribute> attributes = new HashSet<GLVertexAttribute>();

	public void bindAttribute(GLVertexAttribute p, GLBuffer paramData, int bufferComponentIndex) {

		checkNotNull(paramData);
		checkNotNull(p);
		GLBufferMetadata md = checkNotNull(paramData.getBufferMetadata());

		bind();
		paramData.bind();

		int glIndex = p.getLocation();
		int glSize = md.getComponentSize(bufferComponentIndex);
		int glPrimitiveType = md.getGlPrimitiveType();
		int glStride = md.getTotalComponentByteSize();
		int glOffsetPointer = md.getComponentByteOffset(bufferComponentIndex);

		getGl().glVertexAttribPointer(glIndex, glSize, glPrimitiveType, false, glStride, glOffsetPointer);
		attributes.add(p);
		paramData.unbind();
		unbind();
	}

	public void enable(GLVertexAttribute p) {
		checkArgument(attributes.contains(checkNotNull(p)));
		bind();
		getGl().glEnableVertexAttribArray(p.getLocation());
		unbind();
	}
	
	public void disable(GLVertexAttribute p) {
		checkArgument(attributes.contains(checkNotNull(p)));
		bind();
		getGl().glDisableVertexAttribArray(p.getLocation());
		unbind();
	}
	
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
	protected void doUnbind() { getGl().glBindVertexArray(0); }

	@Override
	protected void doDestroy() {
		IntBuffer b = IntBuffer.wrap(new int[] { getGlResourceHandle() });
		b.flip();
		getGl().glDeleteVertexArrays(1, b);
	}
}