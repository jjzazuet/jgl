package org.jgl.opengl.util;

import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.glsl.attribute.GLUniformAttribute;

public class GLUInt extends GLUniformAttribute {

	public GLUInt(int index, int location, int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}


	
	public void set(int value, int index) {
		getProgram().checkBound();
		//getProgram().getGl().glUniform1iv(arg0, arg1, arg2)
		getProgram().getGl().glUniform1i(getLocation(), value);
		getProgram().checkError();
	}
}
