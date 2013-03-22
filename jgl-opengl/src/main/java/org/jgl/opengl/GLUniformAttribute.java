package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;
import org.jgl.math.vector.*;

public class GLUniformAttribute extends GLAttribute {
	
	private GLProgram p;
	
	public GLUniformAttribute(int index, int location, 
			int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name);
		this.p = checkNotNull(p);
	}
	
	public void setFloat(float value) {
		p.checkBound();
		p.getGl().glUniform1f(getLocation(), value);
	}
	
	public void setInt(String uniformName, int value) {
		p.checkBound();
		p.getGl().glUniform1i(getLocation(), value);
	}
	
	public void setVec2f(Vector2 v) {
		p.checkBound();
		p.getGl().glUniform2f(getLocation(), (float) v.x, (float) v.y);
	}

	public void setVec3f(Vector3 v) {
		p.checkBound();
		p.getGl().glUniform3f(getLocation(), (float) v.x, (float) v.y, (float) v.z);
	}
}
