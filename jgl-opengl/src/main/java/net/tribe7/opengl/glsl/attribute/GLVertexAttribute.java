package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;
import net.tribe7.opengl.GLBuffer;
import net.tribe7.opengl.GLBufferMetadata;
import net.tribe7.opengl.GLVertexArray;
import net.tribe7.opengl.glsl.GLProgram;

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
		getProgram().checkBound();
		getVao().bind();
		paramData.bind();

		int glIndex = getLocation();
		int glSize = md.getComponentUnitSize(bufferComponentIndex);
		int glPrimitiveType = md.getGlPrimitiveType();
		int glStride = md.getTotalComponentByteSize();
		int glOffsetPointer = md.getComponentByteOffset(bufferComponentIndex);

		getProgram().getGl().glVertexAttribPointer(
				glIndex, glSize, glPrimitiveType, normalize, glStride, glOffsetPointer);
		getProgram().checkError();
		paramData.unbind();
		getVao().unbind();

		return this;
	}

	public void enable() {
		getProgram().checkBound();
		getVao().bind();
		getVao().getGl().glEnableVertexAttribArray(getLocation());
		getVao().checkError();
		getVao().unbind();
	}

	public void disable() {
		getProgram().checkBound();
		getVao().bind();
		getVao().getGl().glDisableVertexAttribArray(getLocation());
		getVao().checkError();
		getVao().unbind();
	}

	public GLVertexArray getVao() { return vao; }

	public void setVao(GLVertexArray vao) {
		checkNotNull(vao);
		checkArgument(vao.isInitialized());
		this.vao = vao; 
	}
}