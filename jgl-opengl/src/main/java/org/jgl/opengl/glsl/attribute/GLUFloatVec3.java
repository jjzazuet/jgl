package org.jgl.opengl.glsl.attribute;

import static com.google.common.base.Preconditions.checkNotNull;
import org.jgl.math.vector.Vector3;
import org.jgl.opengl.glsl.GLProgram;

public class GLUFloatVec3 extends GLUniformAttribute {

	public GLUFloatVec3(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void set(int index, double x, double y, double z) {
		getProgram().checkBound();
		getProgram().getGl().glUniform3f(getIndexLocation(index), (float) x, (float) y, (float) z);
		getProgram().checkError();
	}
	
	public void set(double x, double y, double z) {
		set(ZERO, x, y, z);
	}

	public void set(Vector3 v) { 
		checkNotNull(v);
		set(ZERO, v.x, v.y, v.z); 
	}

	public void set(int index, Vector3 v) {
		checkNotNull(v);
		set(index, v.x, v.y, v.z); 
	}
}
