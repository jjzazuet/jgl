package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;
import net.tribe7.opengl.GLVertexArray;

public class GLVertexAttributeBinding {

	private final GLVertexAttribute attribute;
	private final GLVertexArray vao;

	protected GLVertexAttributeBinding(GLVertexArray vao, GLVertexAttribute attribute) {
		this.attribute = checkNotNull(attribute);
		this.vao = checkNotNull(vao);
		checkArgument(attribute.getProgram().isInitialized());
		checkArgument(vao.isInitialized());
	}

	public void enable() {
		getAttribute().getProgram().checkBound();
		getVao().bind();
		getVao().getGl().glEnableVertexAttribArray(getAttribute().getLocation());
		getVao().checkError();
		getVao().unbind();
	}

	public void disable() {
		getAttribute().getProgram().checkBound();
		getVao().bind();
		getVao().getGl().glDisableVertexAttribArray(getAttribute().getLocation());
		getVao().checkError();
		getVao().unbind();
	}

	@Override
	public String toString() {
		return String.format("%s[%s -> %s]", getClass().getSimpleName(), getAttribute().getName(), getVao());
	}

	public GLVertexAttribute getAttribute() { return attribute; }
	public GLVertexArray getVao() { return vao; }
}
