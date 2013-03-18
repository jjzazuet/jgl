package org.jgl.opengl;

public class GLVertexAttribute extends GLAttribute {

	public GLVertexAttribute(int location) { super(location); }
	public GLVertexAttribute(int location, String name) { super(location, name); }

	@Override
	public boolean equals(Object o) {
		
		boolean equals = false;
		
		if (o != null && o instanceof GLVertexAttribute) {
			GLVertexAttribute other = (GLVertexAttribute) o;
			equals = getLocation() == other.getLocation();
			if (getName() != null && other.getName() != null) {
				equals = equals && getName().compareTo(other.getName()) == 0; 
			}
		}
		return equals;	
	}
	
	@Override
	public int hashCode() {
		int hc = getLocation();
		if (getName() != null) { hc += getName().hashCode(); }
		return hc;
	}
}
