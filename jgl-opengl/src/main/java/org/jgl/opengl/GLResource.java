package org.jgl.opengl;

import org.jgl.opengl.util.GLCheckError;
import org.slf4j.*;

public class GLResource {

	protected static final Logger log = LoggerFactory.getLogger(GLResource.class);
	private final GLCheckError checkError = new GLCheckError();
	public static final int MINUS_ONE = -1;
	public static final int ZERO = 0;
	
	private int glResourceHandle = MINUS_ONE;
	
	public int getGlResourceHandle() { return glResourceHandle; }
	public GLCheckError getError() { return checkError; }
	
	protected final void setGlResourceHandle(int glResourceHandle) {
		this.glResourceHandle = glResourceHandle;
	}

	@Override
	public boolean equals(Object o) {
		return o != null && 
				o instanceof GLResource && 
				this.getClass().equals(o.getClass()) &&
				this.glResourceHandle == ((GLResource) o).getGlResourceHandle();
	}

	@Override
	public int hashCode() { return glResourceHandle; }
}