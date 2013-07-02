package org.jgl.opengl.glsl.attribute;

import static com.google.common.base.Preconditions.*;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.glsl.GLProgram;

public class GLUFloatVec3 extends GLUniformAttribute<Vector3> {

	public GLUFloatVec3(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void set(int index, double x, double y, double z) {
		checkElementIndex(index, getSize());
		getProgram().checkBound();
		getProgram().getGl().glUniform3f(getIndexLocation(index), (float) x, (float) y, (float) z);
		getProgram().checkError();
	}

	public void set(double x, double y, double z) {
		set(ZERO, x, y, z);
	}

	@Override
	public void doSet(int index, Vector3 value) {
		set(index, value.x, value.y, value.z);
	}
}
