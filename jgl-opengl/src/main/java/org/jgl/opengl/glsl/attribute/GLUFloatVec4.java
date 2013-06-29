package org.jgl.opengl.glsl.attribute;

import static com.google.common.base.Preconditions.*;
import org.jgl.math.vector.Vector4;
import org.jgl.opengl.glsl.GLProgram;

public class GLUFloatVec4 extends GLUniformAttribute {

	public GLUFloatVec4(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void set(int index, double x, double y, double z, double w) {
		getProgram().checkBound();
		getProgram().getGl().glUniform4f(getIndexLocation(index), 
				(float) x, (float) y, (float) z, (float) w);
		getProgram().checkError();
	}
	
	public void set(int index, float [] values) {
		checkNotNull(values);
		checkArgument(values.length == 4);
		set(index, values[0], values[1], values[2], values[3]);
	}

	public void set(double x, double y, double z, double w) {
		set(ZERO, x, y, z, w);
	}

	public void set(Vector4 v) {
		set(ZERO, v.x, v.y, v.z, v.w);
	}

	public void set(int index, Vector4 v) {
		set(index, v.x, v.y, v.z, v.w);
	}
}
