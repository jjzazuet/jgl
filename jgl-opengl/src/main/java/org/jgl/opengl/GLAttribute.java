package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;

public abstract class GLAttribute {

	private final int index, size, glType, location;
	private final String name;
	
	public GLAttribute(int index, int location, int size, int glType, String name) {

		checkArgument(location >= 0);
		checkArgument(index >= 0);
		checkArgument(size >= 0);

		this.name = checkNotNull(name);
		this.index = index;
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
					this.getName().compareTo(other.getName()) == 0 &&
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
				getIndex(), getLocation(), getSize(), getGlType());
	}

	public int getIndex() { return index; }
	public int getSize() { return size; }
	public int getGlType() { return glType; }
	public int getLocation() { return location; }
	public String getName() { return name; }
}
