package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;
import net.tribe7.opengl.GLTexture;
import net.tribe7.opengl.glsl.GLProgram;

public class GLUSampler extends GLUniformAttribute<GLTexture> {

	public GLUSampler(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	@Override
	protected void doSet(int index, GLTexture value) {
		checkElementIndex(index, getSize());
		checkNotNull(value);
		getProgram().checkBound();
		value.setActive();
		value.bind(); {
			getProgram().getGl().glUniform1i(getIndexLocation(index), value.getTextureUnit());
			getProgram().checkError();
		} value.unbind();
	}

	@Override
	public int getUnitByteSize() {
		throw new UnsupportedOperationException();
	}
}
