package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;
import static java.lang.String.format;

import java.nio.IntBuffer;

import javax.media.opengl.GL3;

public abstract class GLContextBoundResource extends GLResource {

	private GL3 gl = null;
	private boolean bound = false;
	private boolean initialized = false;

	protected abstract void doInit();
	protected abstract void doBind();
	protected abstract void doUnbind();
	protected abstract void doDestroy();
	
	public void init(final GL3 gl) {
		
		this.gl = checkNotNull(gl);
		
		if (!initialized) {	
			doInit();
			if (log.isDebugEnabled()) {
				checkError().apply(gl);
				log.debug(resourceMsg("Initialized GL resource"));
			}
			initialized = true;
		}
	}
	
	public void bind() {
		if (!initialized) { 
			throw new IllegalStateException(resourceMsg("Resouce not initialized")); 
		}
		if (bound) { multipleBindWarn(); }
		doBind();
		checkError().apply(gl);
		bound = true;
	}
	
	public void unbind() {
		if (!bound) { multipleBindWarn(); }
		doUnbind();
		checkError().apply(gl);
		bound = false;
	}
		
	public boolean isInitialized() { return initialized; }
	
	public void checkInitialized() {
		checkState(initialized, resourceMsg("GL resource not initialized"));
	}
	
	/** Perform OpenGL resource deallocation. */
	public void destroy() {
		doDestroy();
		if (log.isDebugEnabled()) {
			log.debug(resourceMsg("GL resource destroyed"));
		}
	}
	
	/** Determine if this object is bound to the OpenGL context. */
	public boolean isBound() { return bound; }
	
	/** Verify bind state.
	 * @throws IllegalStateException if the resource is not bound to the OpenGL context. */
	public void checkBound() {
		checkState(bound, resourceMsg("Unbound GL resource"));
	}
	
	@Override
	public String toString() {
		return String.format("%s[rh: %s, bound: %s, init: %s]", 
				getClass().getSimpleName(), getGlResourceHandle(), isBound(), initialized);
	}
	
	private void multipleBindWarn() {
		log.warn(resourceMsg("Multiple bind/unbind request"));		
	}
	
	public String resourceMsg(String msg) {
		return format("%s: [%s, %s]", 
				msg, getClass().getSimpleName(), getGlResourceHandle());
	}

	protected final IntBuffer intReadBuffer(int i) {
		IntBuffer b = IntBuffer.wrap(new int [] {i});
		b.flip();
		return b;
	}

	public GL3 getGl() { return gl; }
}