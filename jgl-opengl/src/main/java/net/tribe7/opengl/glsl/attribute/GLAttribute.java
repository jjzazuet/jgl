package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Preconditions.*;
import net.tribe7.opengl.glsl.GLProgram;

public abstract class GLAttribute extends GLProgramVariable {

	private final int size, glType, location;

	public GLAttribute(int index, int location, int size, int glType, String name, GLProgram p) {

		super(index, name, p);
		checkArgument(index >= ZERO);
		checkArgument(size >= ZERO);

		this.location = location;
		this.size = size;
		this.glType = glType;
	}

	@Override
	public boolean equals(Object o) {

		boolean equals = false;

		if (o != null && o instanceof GLAttribute) {

			GLAttribute other = (GLAttribute) o;

			equals = 
					this.getName().compareTo(other.getName()) == ZERO &&
					this.getIndex() == other.getIndex() &&
					this.getLocation() == other.getLocation() &&
					this.getSize() == other.getSize() &&
					this.getGlType() == other.getGlType();
		}
		return equals;
	}

	@Override
	public int hashCode() {
		return getName().hashCode() + getIndex() + 
				getLocation() + getSize() + getGlType();
	}

	@Override
	public String toString() {
		return String.format("%s [name:%s, idx:%s, loc:%s, size:%s, glType:%s]", 
				getClass().getSimpleName(), getName(), 
				getIndex(), getLocation(), getSize(), 
				Integer.toHexString(getGlType()));
	}

	public int getSize() { return size; }
	public int getGlType() { return glType; }
	public int getLocation() { return location; }
}
