package net.tribe7.opengl;

import net.tribe7.opengl.util.GLCheckError;
import org.slf4j.*;
import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.math.Preconditions.*;

public class GLResource {

	protected static final Logger log = LoggerFactory.getLogger(GLResource.class);
	private final GLCheckError checkError = new GLCheckError();
	private int glResourceHandle = MINUS_ONE;

	public int getGlResourceHandle() { return glResourceHandle; }
	public GLCheckError getError() { return checkError; }

	protected final void setGlResourceHandle(int glResourceHandle) {
		checkArgument(glResourceHandle > MINUS_ONE);
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
	public int hashCode() { 
		return getClass().getCanonicalName().hashCode() + glResourceHandle; 
	}
}