package org.jgl.opengl;

import static org.jgl.math.matrix.Matrix2Ops.*;
import static org.jgl.math.matrix.Matrix4Ops.*;
import static org.jgl.math.Preconditions.*;
import static com.google.common.base.Preconditions.*;

import java.nio.FloatBuffer;

import org.jgl.math.matrix.Matrix2;
import org.jgl.math.matrix.Matrix4;
import org.jgl.math.vector.*;

public class GLUniformAttribute extends GLAttribute {
		
	public GLUniformAttribute(int index, int location, 
			int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}
	
	public void setFloat(float value) {
		getProgram().checkBound();
		getProgram().getGl().glUniform1f(getLocation(), value);
	}
	
	public void setInt(int value) {
		getProgram().checkBound();
		getProgram().getGl().glUniform1i(getLocation(), value);
	}
	
	public void setMat2fv(boolean transpose, FloatBuffer dst, Matrix2 ... src) {
		getProgram().checkBound();
		checkNoNulls(src);
		checkArgument(src.length >= 1);
		store(dst, src);
		getProgram().getGl().glUniformMatrix2fv(getLocation(), src.length, transpose, dst);
		getProgram().checkError().apply(getProgram().getGl());
	}
	
	public void setMat4fv(boolean transpose, FloatBuffer dst, Matrix4 ... src) {
		getProgram().checkBound();
		checkNoNulls(src);
		checkArgument(src.length >= 1);
		store(dst, src);
		getProgram().getGl().glUniformMatrix4fv(getLocation(), src.length, transpose, dst);
		getProgram().checkError().apply(getProgram().getGl());
	}
	
	public void setVec2f(double x, double y) {
		getProgram().checkBound();
		getProgram().getGl().glUniform2f(getLocation(), (float) x, (float) y);		
	}
	
	public void setVec2f(Vector2 v) { 
		checkNotNull(v);
		setVec2f(v.x, v.y); 
	}

	public void setVec3f(double x, double y, double z) {
		getProgram().checkBound();
		getProgram().getGl().glUniform3f(getLocation(), (float) x, (float) y, (float) z);		
	}
	
	public void setVec3f(Vector3 v) { 
		checkNotNull(v);
		setVec3f(v.x, v.y, v.z); 
	}

	public void setVec2fv(float [] data) {
		getProgram().checkBound();
		getProgram().getGl().glUniform2fv(getLocation(), getSize(), bufferData(data, 2));
		getProgram().checkError().apply(getProgram().getGl());
	}
	
	public void setVec3fv(float [] data) {
		getProgram().checkBound();
		getProgram().getGl().glUniform3fv(getLocation(), getSize(), bufferData(data, 3));
		getProgram().checkError().apply(getProgram().getGl());
	}
	
	public void setVec4fv(float [] data) {
		getProgram().checkBound();
		getProgram().getGl().glUniform4fv(getLocation(), getSize(), bufferData(data, 4));
		getProgram().checkError().apply(getProgram().getGl());
	}
	
	protected FloatBuffer bufferData(float [] data, int componentSize) {
		checkNotNull(data);
		checkArgument(componentSize >= 1);
		checkArgument(data.length >= componentSize);
		checkArgument(data.length / componentSize == getSize());
		FloatBuffer fb = FloatBuffer.wrap(data);
		fb.flip();
		return fb;
	}
}