package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;

public class GLVertexAttribute extends GLAttribute {

	private GLVertexArray vao;
	
	public GLVertexAttribute(int index, int location, int size, 
			int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}
	
	public GLVertexAttribute set(GLVertexArray vao, GLBuffer paramData, boolean normalize, int bufferComponentIndex) {

		setVao(vao);
		checkNotNull(paramData);
		checkArgument(bufferComponentIndex >= 0);
		
		GLBufferMetadata md = checkNotNull(paramData.getBufferMetadata());

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
		checkArgument(vao.isInitialized());
		this.vao = checkNotNull(vao); 
	}
}