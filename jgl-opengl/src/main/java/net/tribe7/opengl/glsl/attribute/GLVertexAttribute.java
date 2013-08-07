package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;

import java.util.HashMap;
import java.util.Map;

import net.tribe7.opengl.GLBuffer;
import net.tribe7.opengl.GLBufferMetadata;
import net.tribe7.opengl.GLVertexArray;
import net.tribe7.opengl.glsl.GLProgram;

public class GLVertexAttribute extends GLAttribute {

	private final Map<GLVertexArray, GLVertexAttributeBinding> bindings = new HashMap<GLVertexArray, GLVertexAttributeBinding>();

	public GLVertexAttribute(int index, int location, int size,  int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public GLVertexAttributeBinding set(GLVertexArray vao, GLBuffer paramData, boolean normalize, int bufferComponentIndex) {

		checkNotNull(vao);
		checkNotNull(paramData);
		getProgram().checkBound();

		GLVertexAttributeBinding vaoBinding = getBindings().get(vao);

		if (vaoBinding == null) {

			vaoBinding = new GLVertexAttributeBinding(vao, this);

			GLBufferMetadata md = checkNotNull(paramData.getBufferMetadata());
			checkElementIndex(bufferComponentIndex, md.getComponentCount());
			// TODO add some kind of raw type checking e.g. float(3) = vec3

			vao.bind(); {
				int glIndex = getLocation();
				int glSize = md.getComponentUnitSize(bufferComponentIndex);
				int glPrimitiveType = md.getGlPrimitiveType();
				int glStride = md.getTotalComponentByteSize();
				int glOffsetPointer = md.getComponentByteOffset(bufferComponentIndex);

				paramData.bind();
				getProgram().getGl().glVertexAttribPointer(glIndex, glSize, glPrimitiveType, normalize, glStride, glOffsetPointer);
				getProgram().checkError();
				paramData.unbind();
			} vao.unbind();

			getBindings().put(vao, vaoBinding);
		}

		return vaoBinding;
	}

	public Map<GLVertexArray, GLVertexAttributeBinding> getBindings() {
		return bindings;
	}
}