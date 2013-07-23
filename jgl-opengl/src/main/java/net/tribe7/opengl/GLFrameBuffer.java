package net.tribe7.opengl;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.opengl.GLConstants.*;
import static net.tribe7.opengl.util.GLBufferUtils.*;

import java.nio.IntBuffer;

public abstract class GLFrameBuffer extends GLContextBoundResource {

	private int bindTarget = MINUS_ONE;

	protected abstract void doInitAttachments();

	public void initAttachments() {
		checkBound();
		doInitAttachments();
		int fbStatus = getGl().glCheckFramebufferStatus(GL_FRAMEBUFFER);
		checkError();
		checkState(fbStatus == GL_FRAMEBUFFER_COMPLETE, 
				resourceMsg("Unable to initialize Framebuffer [%s]"), 
				Integer.toHexString(fbStatus));
		if (log.isDebugEnabled()) {
			log.debug(resourceMsg("Framebuffer attachments initialized."));
		}
	}

	protected void setAttachment(int attachmentTarget, GLTexture t) {
		checkArgument(attachmentTarget >= GL_COLOR_ATTACHMENT0);
		getGl().glFramebufferTexture(getBindTarget(), attachmentTarget,
				t.getGlResourceHandle(), ZERO);
		checkError();
	}

	protected void setRenderBuffer(int attachmentTarget, GLRenderBuffer rb) {
		checkBound();
		checkArgument(isValidDepthStencilAttachment(attachmentTarget));
		getGl().glFramebufferRenderbuffer(getBindTarget(),
				attachmentTarget, GL_RENDERBUFFER, rb.getGlResourceHandle());
		checkError();
	}

	@Override
	protected void doInit() {
		IntBuffer ib = IntBuffer.allocate(ONE);
		getGl().glGenFramebuffers(ONE, ib);
		setGlResourceHandle(ib.get());
	}

	@Override
	protected void doBind() {
		getGl().glBindFramebuffer(getBindTarget(), getGlResourceHandle());
	}

	@Override
	protected void doUnbind() {
		getGl().glBindFramebuffer(getBindTarget(), ZERO);
	}

	@Override
	protected void doDestroy() {
		getGl().glDeleteFramebuffers(ONE, intBuffer(getGlResourceHandle()));
	}

	public int getBindTarget() { 
		checkState(bindTarget != MINUS_ONE);
		return bindTarget; 
	}

	public void setBindTarget(int newTarget) {
		checkArgument(GL_FRAMEBUFFER_TARGET.contains(newTarget));
		this.bindTarget = newTarget;
	}

	public boolean isValidDepthStencilFormat(int format) {
		return format == GL_DEPTH_COMPONENT || format == GL_DEPTH_STENCIL;
	}

	public boolean isValidDepthStencilAttachment(int target) {
		return target == GL_DEPTH_ATTACHMENT || target == GL_STENCIL_ATTACHMENT;
	}
}