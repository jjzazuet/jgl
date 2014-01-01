package net.tribe7.opengl;

import javax.media.opengl.GL3;

import static java.lang.String.format;
import static net.tribe7.common.base.Preconditions.*;

public abstract class GLContextResource extends GLResource {

	private GL3 gl = null;

	public GL3 getGl() { return gl; }

	protected void setGl(GL3 gl) { 
		checkState(this.gl == null, "GL context already assigned: [%s]", this.gl);
		this.gl = checkNotNull(gl); 
	}

	public void checkError() { getError().apply(getGl()); }

	public String resourceMsg(String msg) {
		return format("%s: [%s, %s]", 
				msg, getClass().getSimpleName(), getGlResourceHandle());
	}
}
