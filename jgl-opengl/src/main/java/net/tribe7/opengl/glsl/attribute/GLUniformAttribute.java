package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.opengl.util.GLBufferUtils.*;
import static net.tribe7.common.base.Preconditions.*;

import java.nio.*;
import java.util.*;

import javax.media.opengl.GL3;

import net.tribe7.opengl.glsl.GLProgram;

public abstract class GLUniformAttribute<T> extends GLAttribute {

	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final String VARIABLE_INDEX_FORMAT = "%s[%s]";
	
	private Map<Integer, Integer> indexLocations = new HashMap<Integer, Integer>();

	public GLUniformAttribute(int index, int location, 
			int size, int glType, String name, GLProgram p) {
		super(index, location, size, glType, name, p);
	}

	public void set(int index, T value) {
		checkElementIndex(index, getSize());
		checkNotNull(value);
		doSet(index, value);
	}

	public void set(T value) { set(ZERO, value); }
	protected abstract void doSet(int index, T value);

	protected FloatBuffer bufferData(float ... data) {
		checkNotNull(data);
		checkArgument(data.length <= getSize());
		checkArgument(data.length % getSize() == 0);
		return floatBuffer(data);
	}

	protected IntBuffer bufferData(int ... data) {
		checkNotNull(data);
		checkArgument(data.length <= getSize());
		checkArgument(data.length % getSize() == 0);
		return intBuffer(data);
	}

	protected String variableIndex(String name, int index) {
		checkNotNull(name);
		String result = String.format(VARIABLE_INDEX_FORMAT, name, index);
		return result;
	}

	protected int getIndexLocation(int index) {

		checkArgument(index >= 0);
		checkElementIndex(index, getSize());

		if (indexLocations.isEmpty()) {
			
			GLProgram p = getProgram();
			GL3 gl = p.getGl();
			p.checkBound();

			if (getSize() > ONE) {
				for (int k = 0; k < getSize(); k++) {
					int location = gl.glGetUniformLocation(
							p.getGlResourceHandle(), 
							variableIndex(getName(), k));
					p.checkError();
					indexLocations.put(k, location);
				}
			} else {
				indexLocations.put(ZERO, getLocation());
			}
		}
		return indexLocations.get(index);
	}
}