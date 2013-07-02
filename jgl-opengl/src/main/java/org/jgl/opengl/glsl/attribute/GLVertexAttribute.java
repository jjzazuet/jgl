package org.jgl.opengl.glsl.attribute;

import static com.google.common.base.Preconditions.*;

import org.jgl.opengl.GLBuffer;
import org.jgl.opengl.GLBufferMetadata;
import org.jgl.opengl.GLVertexArray;
import org.jgl.opengl.glsl.GLProgram;

public class GLVertexAttribute extends GLAttribute {

	private GLVertexArray vao;
	
	public GLVertexAttribute(int index, int location, int size, 
			int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}
	
	public GLVertexAttribute set(GLVertexArray vao, GLBuffer paramData, boolean normalize, int bufferComponentIndex) {

		setVao(vao);
		checkNotNull(paramData);
		GLBufferMetadata md = checkNotNull(paramData.getBufferMetadata());
		checkElementIndex(bufferComponentIndex, md.getComponentCount());

		// TODO add some kind of raw type checking e.g. float(3) = vec3
		getVao().bind();
		paramData.bind();

		int glIndex = getLocation();
		int glSize = md.getComponentUnitSize(bufferComponentIndex);
		int glPrimitiveType = md.getGlPrimitiveType();
		int glStride = md.getTotalComponentByteSize();
		int glOffsetPointer = md.getComponentByteOffset(bufferComponentIndex);

		getProgram().getGl().glVertexAttribPointer(
				glIndex, glSize, glPrimitiveType, normalize, glStride, glOffsetPointer);
		paramData.unbind();
		getVao().unbind();

		return this;
	}

	public void enable() {
		getVao().bind();
		getVao().getGl().glEnableVertexAttribArray(getLocation());
		getVao().unbind();
	}

	public void disable() {
		getVao().bind();
		getVao().getGl().glDisableVertexAttribArray(getLocation());
		getVao().unbind();
	}

	public GLVertexArray getVao() { return vao; }

	public void setVao(GLVertexArray vao) {
		checkNotNull(vao);
		checkArgument(vao.isInitialized());
		this.vao = vao; 
	}
}