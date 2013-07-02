package org.jgl.opengl.glsl.attribute;

import static com.google.common.base.Preconditions.*;
import org.jgl.math.vector.Vector2;
import org.jgl.opengl.glsl.GLProgram;

public class GLUFloatVec2 extends GLUniformAttribute<Vector2> {

	public GLUFloatVec2(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void set(int index, double x, double y) {
		checkElementIndex(index, getSize());
		getProgram().checkBound();
		getProgram().getGl().glUniform2f(getIndexLocation(index), (float) x, (float) y);
		getProgram().checkError();
	}

	public void set(double x, double y) {
		set(ZERO, x, y);
	}

	@Override
	public void doSet(int index, Vector2 value) {
		set(index, value.x, value.y);
	}
}
