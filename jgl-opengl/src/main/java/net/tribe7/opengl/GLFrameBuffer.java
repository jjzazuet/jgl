package net.tribe7.opengl;

import static net.tribe7.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.opengl.GLConstants.*;
import static net.tribe7.opengl.util.GLBufferUtils.*;

import java.nio.IntBuffer;
import java.util.*;
import java.util.Map.Entry;

public class GLFrameBuffer extends GLContextBoundResource {

	private int bindTarget = MINUS_ONE;
	private Map<Integer, GLTexture2D> colorAttachments = new HashMap<Integer, GLTexture2D>();
	private Map<Integer, Integer> colorAttachmentParameters = new HashMap<Integer, Integer>();
	private GLTextureMetadata colorAttachmentFormat = new GLTextureMetadata();
	private GLRenderBuffer depthStencilBuffer = new GLRenderBuffer();

	public void initAttachments() {

		checkBound();
		checkState(getDepthStencilBuffer().getBufferFormat().getWidth() ==
				getColorAttachmentFormat().getWidth());
		checkState(getDepthStencilBuffer().getBufferFormat().getHeight() ==
				getColorAttachmentFormat().getHeight());

		GLTexture2DImage attachmentImage = new GLTexture2DImage();
		attachmentImage.getMetadata().setFrom(getColorAttachmentFormat());

		for (Entry<Integer, GLTexture2D> attachmentEntry : getColorAttachments().entrySet()) {

			GLTexture2D colorAttachment = attachmentEntry.getValue();
			int glColorAttachment = GL_COLOR_ATTACHMENT0 + attachmentEntry.getKey();

			colorAttachment.init(getGl());
			colorAttachment.bind();
			colorAttachment.loadData(attachmentImage);

			for (Entry<Integer, Integer> colorParam : getColorAttachmentParameters().entrySet()) {
				colorAttachment.setParameter(colorParam.getKey(), colorParam.getValue());
			}

			getGl().glFramebufferTexture2D(getBindTarget(), glColorAttachment,
					colorAttachment.getTextureTarget(),
					colorAttachment.getGlResourceHandle(), ZERO);
			checkError();
			colorAttachment.unbind();
		}

		depthStencilBuffer.init(getGl());
		depthStencilBuffer.bind();
		depthStencilBuffer.initStorage();

		int depthStencilInternalFormat = depthStencilBuffer.getBufferFormat().getInternalFormat();
		checkState(depthStencilInternalFormat == GL_DEPTH_COMPONENT
				|| depthStencilInternalFormat == GL_DEPTH_STENCIL);

		if (depthStencilInternalFormat == GL_DEPTH_COMPONENT) {
			getGl().glFramebufferRenderbuffer(getBindTarget(),
					GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthStencilBuffer.getGlResourceHandle());
			checkError();
		} else if (depthStencilInternalFormat == GL_DEPTH_STENCIL) {
			getGl().glFramebufferRenderbuffer(getBindTarget(),
					GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthStencilBuffer.getGlResourceHandle());
			checkError();
			getGl().glFramebufferRenderbuffer(getBindTarget(), 
					GL_STENCIL_ATTACHMENT, GL_RENDERBUFFER, depthStencilBuffer.getGlResourceHandle());
			checkError();
		}
		depthStencilBuffer.unbind();

		int fbStatus = getGl().glCheckFramebufferStatus(GL_FRAMEBUFFER);
		checkError();
		checkState(fbStatus == GL_FRAMEBUFFER_COMPLETE, 
				resourceMsg("Unable to initialize Framebuffer [%s]"), 
				Integer.toHexString(fbStatus));

		if (log.isDebugEnabled()) {
			log.debug(resourceMsg("Framebuffer attachments initialized."));
		}
	}

	public GLTexture2D getColorAttachment(int colorAttachmentIndex) {
		checkElementIndex(colorAttachmentIndex, getColorAttachments().size());
		return getColorAttachments().get(colorAttachmentIndex);
	}

	// TODO enable attachment of cube map textures
	public void setColorAttachment(int colorAttachmentIndex) {
		checkArgument(colorAttachmentIndex >= ZERO);
		checkArgument(
				!getColorAttachments().containsKey(colorAttachmentIndex),
				resourceMsg("Color attachment already set."));
		colorAttachments.put(colorAttachmentIndex, new GLTexture2D());
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

	public Map<Integer, GLTexture2D> getColorAttachments() { return colorAttachments; }
	public Map<Integer, Integer> getColorAttachmentParameters() { return colorAttachmentParameters; }
	public GLTextureMetadata getColorAttachmentFormat() { return colorAttachmentFormat; }
	public GLRenderBuffer getDepthStencilBuffer() { return depthStencilBuffer; }

	public int getBindTarget() { 
		checkState(bindTarget != MINUS_ONE);
		return bindTarget; 
	}

	public void setBindTarget(int newTarget) {
		checkArgument(GL_FRAMEBUFFER_TARGET.contains(newTarget));
		this.bindTarget = newTarget;
	}
}