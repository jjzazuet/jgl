package org.jgl.opengl.glsl.attribute;

import static com.google.common.base.Preconditions.*;
import org.jgl.math.vector.Vector2;
import org.jgl.opengl.glsl.GLProgram;

public class GLUFloatVec2 extends GLUniformAttribute {

	public GLUFloatVec2(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void set(int index, double x, double y) {
		getProgram().checkBound();
		getProgram().getGl().glUniform2f(getIndexLocation(index), (float) x, (float) y);
		getProgram().checkError();
	}

	public void set(double x, double y) {
		set(ZERO, x, y);
	}
	
	public void set(Vector2 v) { 
		checkNotNull(v);
		set(ZERO, v.x, v.y);
	}

	public void set(int index, Vector2 v) {
		set(index, v.x, v.y);
	}
	
	/*
	public void setVec2fv(float [] data) {
		checkNotNull(data);
		checkArgument(data.length == 2);
		getProgram().checkBound();
		getProgram().getGl().glUniform2fv(getLocation(), getSize(), bufferData(data, 2));
		getProgram().checkError();
	}*/

}
