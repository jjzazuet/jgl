package org.jgl.opengl;

import org.jgl.opengl.util.GlCheckError;
import org.slf4j.*;

public class GLResource {

	protected static final Logger log = LoggerFactory.getLogger(GLResource.class);
	private final GlCheckError checkError = new GlCheckError();

	private int glResourceHandle = -1;
	
	public int getGlResourceHandle() { return glResourceHandle; }
	public GlCheckError checkError() { return checkError; }
	
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