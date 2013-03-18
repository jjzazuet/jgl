package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;

public abstract class GLAttribute {

	private int location = -1;
	private String name = null;
	
	public GLAttribute(int location) { setLocation(location); }
	
	public GLAttribute(int location, String name) {
		this(location);
		this.name = checkNotNull(name);
	}

	protected void setLocation(int location) {
		checkArgument(location >= 0);
		this.location = location;		
	}

	@Override
	public String toString() {
		return String.format("%s [%s, %s]", getClass().getSimpleName(), getName(), getLocation());
	}
	
	public String getName() { return name; }
	public int getLocation() { return location; }
}
