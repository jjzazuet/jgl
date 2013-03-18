package org.jgl.opengl;

public class GLUniformAttribute extends GLAttribute {

	private int index;
	private int size;
	private int glType;
	
	public GLUniformAttribute(int index, int location, int size, int glType, String name) {
		super(location, name);
		this.index = index;
		this.size = size;
		this.glType = glType;
	}
	
	@Override
	public boolean equals(Object o) {
		
		boolean equals = false;
		
		if (o != null && o instanceof GLUniformAttribute) {
			
			GLUniformAttribute other = (GLUniformAttribute) o;
			
			equals = this.index == other.getIndex() &&
					this.size == other.getSize() &&
					this.glType == other.getGlType() &&
					this.getName().compareTo(other.getName()) == 0;
		}
		return equals;
	}
	
	@Override
	public int hashCode() {
		return index + getLocation() + size + glType + getName().hashCode();
	}
	
	public int getIndex() { return index; }
	public int getSize() { return size; }
	public int getGlType() { return glType; }
}
