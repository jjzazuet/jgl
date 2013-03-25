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
	
	public void setInt(int value) {
		p.checkBound();
		p.getGl().glUniform1i(getLocation(), value);
	}
	
	public void setMat2fv(boolean transpose, FloatBuffer dst, Matrix2 ... src) {
		p.checkBound();
		checkNoNulls(src);
		checkArgument(src.length >= 1);
		store(dst, src);
		p.getGl().glUniformMatrix2fv(getLocation(), src.length, transpose, dst);
		p.checkError().apply(p.getGl());
	}
	
	public void setMat4fv(boolean transpose, FloatBuffer dst, Matrix4 ... src) {
		p.checkBound();
		checkNoNulls(src);
		checkArgument(src.length >= 1);
		store(dst, src);
		p.getGl().glUniformMatrix4fv(getLocation(), src.length, transpose, dst);
		p.checkError().apply(p.getGl());
	}
	
	public void setVec2f(double x, double y) {
		p.checkBound();
		p.getGl().glUniform2f(getLocation(), (float) x, (float) y);		
	}
	
	public void setVec2f(Vector2 v) { 
		checkNotNull(v);
		setVec2f(v.x, v.y); 
	}

	public void setVec3f(double x, double y, double z) {
		p.checkBound();
		p.getGl().glUniform3f(getLocation(), (float) x, (float) y, (float) z);		
	}
	
	public void setVec3f(Vector3 v) { 
		checkNotNull(v);
		setVec3f(v.x, v.y, v.z); 
	}

	public void setVec2fv(float [] data) {
		p.checkBound();
		p.getGl().glUniform2fv(getLocation(), getSize(), bufferData(data, 2));
		p.checkError().apply(p.getGl());
	}
	
	public void setVec3fv(float [] data) {
		p.checkBound();
		p.getGl().glUniform3fv(getLocation(), getSize(), bufferData(data, 3));
		p.checkError().apply(p.getGl());
	}
	
	public void setVec4fv(float [] data) {
		p.checkBound();
		p.getGl().glUniform4fv(getLocation(), getSize(), bufferData(data, 4));
		p.checkError().apply(p.getGl());
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