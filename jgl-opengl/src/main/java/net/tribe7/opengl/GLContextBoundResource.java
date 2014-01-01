package net.tribe7.opengl;

import static net.tribe7.common.base.Preconditions.*;
import javax.media.opengl.GL3;

public abstract class GLContextBoundResource extends GLContextResource {

	private boolean bound = false;
	private boolean initialized = false;
	private boolean destroyed = false;

	protected abstract int doInit();
	protected abstract void doDestroy();
	protected abstract void doBind();
	protected abstract void doUnbind();

	public void init(final GL3 gl) {
		if (!isInitialized()) {
			setGl(gl);
			setGlResourceHandle(doInit());
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

	@Override
	public String toString() {
		return String.format("%s[rh: %s, bound: %s, init: %s]", 
				getClass().getSimpleName(), getGlResourceHandle(), isBound(), initialized);
	}

	private String multipleBindError() {
		return resourceMsg("Multiple bind/unbind request");
	}
}