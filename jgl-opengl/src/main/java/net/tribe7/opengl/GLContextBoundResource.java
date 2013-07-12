package net.tribe7.opengl;

import static net.tribe7.common.base.Preconditions.*;
import static java.lang.String.format;
import javax.media.opengl.GL3;

public abstract class GLContextBoundResource extends GLResource {

	private GL3 gl = null;
	private boolean bound = false;
	private boolean initialized = false;
	private boolean destroyed = false;

	protected abstract void doInit();
	protected abstract void doBind();
	protected abstract void doUnbind();
	protected abstract void doDestroy();
	
	public void init(final GL3 gl) {
		
		this.gl = checkNotNull(gl);
		
		if (!isInitialized()) {	
			doInit();
			checkError();
			initialized = true;
			if (log.isDebugEnabled()) {
				log.debug(resourceMsg("GL resource initialized."));
			}
		} else if (log.isDebugEnabled()) {
			log.debug(resourceMsg("GL resource already initialized."));
		}
	}
	
	public void bind() {
		checkInitialized();
		checkState(!isBound(), multipleBindError());
		doBind();
		checkError();
		bound = true;
	}
	
	public void unbind() {
		checkState(isBound(), multipleBindError());
		doUnbind();
		checkError();
		bound = false;
	}

	/** Perform OpenGL resource deallocation. Can only be called once per resource. */
	public void destroy() {
		checkState(!isDestroyed(), resourceMsg("GL resource already destroyed!"));
		doDestroy();
		checkError();
		destroyed = true;
		if (log.isDebugEnabled()) {
			log.debug(resourceMsg("GL resource destroyed."));
		}
	}

	/** Determine if this object is bound to the OpenGL context. */
	public boolean isBound() { return bound; }
	public boolean isInitialized() { return initialized; }
	public boolean isDestroyed() { return destroyed; }
	
	public void checkInitialized() {
		checkState(initialized, resourceMsg("GL resource not initialized."));
	}

	/** Verify bind state.
	 * @throws IllegalStateException if the resource is not bound to the OpenGL context. */
	public void checkBound() {
		checkState(bound, resourceMsg("Unbound GL resource"));
	}

	public void checkError() { getError().apply(gl); }

	@Override
	public String toString() {
		return String.format("%s[rh: %s, bound: %s, init: %s]", 
				getClass().getSimpleName(), getGlResourceHandle(), isBound(), initialized);
	}

	private String multipleBindError() {
		return resourceMsg("Multiple bind/unbind request");
	}

	public String resourceMsg(String msg) {
		return format("%s: [%s, %s]", 
				msg, getClass().getSimpleName(), getGlResourceHandle());
	}

	public GL3 getGl() { return gl; }
}