package org.jgl.opengl.glsl.attribute;

import static org.jgl.math.matrix.Matrix2Ops.*;
import static org.jgl.math.matrix.Matrix4Ops.*;
import static org.jgl.math.Preconditions.*;
import static com.google.common.base.Preconditions.*;

import java.nio.FloatBuffer;
import javax.media.opengl.GL3;
import org.jgl.math.matrix.*;
import org.jgl.math.matrix.io.BufferedMatrix4;
import org.jgl.math.vector.*;
import org.jgl.opengl.glsl.GLProgram;

public class GLUniformAttribute extends GLAttribute {

	public static final String VARIABLE_INDEX_FORMAT = "%s[%s]";

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
		storeColMaj(dst, src);
		getProgram().getGl().glUniformMatrix2fv(getLocation(), src.length, transpose, dst);
		getProgram().checkError().apply(getProgram().getGl());
	}

	public void setMat4fv(boolean transpose, FloatBuffer dst, Matrix4 ... src) {
		getProgram().checkBound();
		checkNoNulls(src);
		checkArgument(src.length >= 1);
		storeColMaj(dst, src);
		getProgram().getGl().glUniformMatrix4fv(getLocation(), src.length, transpose, dst);
		getProgram().checkError().apply(getProgram().getGl());
	}

	public void setMat4fv(boolean transpose, BufferedMatrix4 src) {
		setMat4fv(transpose, src.getBackingBuffer(), src);
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
		checkNotNull(data);
		checkArgument(data.length == 2);
		getProgram().checkBound();
		getProgram().getGl().glUniform2fv(getLocation(), getSize(), bufferData(data, 2));
		getProgram().checkError().apply(getProgram().getGl());
	}

	public void setVec3fv(float [] data) {
		checkNotNull(data);
		checkArgument(data.length % 3 == 0);
		checkArgument(data.length == getSize() * 3);
		getProgram().checkBound();
		getProgram().getGl().glUniform3fv(getLocation(), getSize(), bufferData(data, 3));
		getProgram().checkError().apply(getProgram().getGl());
	}

	public void setVec4fv(float [] data) {
		checkNotNull(data);
		checkArgument(data.length % 4 == 0);
		checkArgument(data.length == getSize() * 4);
		getProgram().checkBound();
		getProgram().getGl().glUniform4fv(getLocation(), getSize(), bufferData(data, 4));
		getProgram().checkError().apply(getProgram().getGl());
	}

	public void setVec4fv(float [] data, int index) { // TODO implement indexed access for other data types.
		checkElementIndex(index, getSize());
		getProgram().getGl().glUniform4fv(getIndexLocation(index), 1, bufferData(data, 4));
		getProgram().checkError().apply(getProgram().getGl());
	}

	protected FloatBuffer bufferData(float [] data, int componentSize) {
		checkNotNull(data);
		checkArgument(componentSize >= 1);
		checkArgument(data.length >= componentSize);
		checkArgument(data.length % componentSize == 0);
		checkArgument(data.length / componentSize <= getSize());
		FloatBuffer fb = FloatBuffer.wrap(data);
		fb.flip();
		return fb;
	}
	
	protected String variableIndex(String name, int index) {
		checkNotNull(name);
		String result = String.format(VARIABLE_INDEX_FORMAT, name, index);
		return result;
	}
	
	protected int getIndexLocation(int index) {

		checkArgument(index >= 0);		
		GLProgram p = getProgram();
		GL3 gl = p.getGl();
		p.checkBound();

		int location = gl.glGetUniformLocation(p.getGlResourceHandle(), 
				variableIndex(getName(), index));

		p.checkError().apply(gl);
		return location;
	}
}