package org.jgl.opengl;

import static org.jgl.opengl.GLConstants.*;
import static com.google.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static org.jgl.opengl.util.GLBufferUtils.*;
import java.nio.IntBuffer;
import java.util.*;
import java.util.Map.Entry;

public class GLFrameBuffer extends GLContextBoundResource {

	private int bindMode = MINUS_ONE;
	private boolean attachmentsInitialized = false;	
	private Map<Integer, GLTexture2D> colorAttachments = new HashMap<Integer, GLTexture2D>();
	private Map<Integer, Integer> colorAttachmentParameters = new HashMap<Integer, Integer>();
	private GLTextureMetadata colorAttachmentFormat = new GLTextureMetadata();
	private GLRenderBuffer depthStencilBuffer = new GLRenderBuffer();

	public void initAttachments() {

		bind();
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
			colorAttachment.setTextureTarget(GL_TEXTURE_2D);
			colorAttachment.bind();
			colorAttachment.loadData(attachmentImage);

			for (Entry<Integer, Integer> colorParam : getColorAttachmentParameters().entrySet()) {
				colorAttachment.setParameter(
						colorParam.getKey(), colorParam.getValue());
			}

			colorAttachment.unbind();
			getGl().glFramebufferTexture2D(
					GL_FRAMEBUFFER, glColorAttachment, 
					colorAttachment.getTextureTarget(), 
					colorAttachment.getGlResourceHandle(), ZERO);
			checkError();
		}

		depthStencilBuffer.init(getGl());
		depthStencilBuffer.initStorage();

		int depthStencilInternalFormat = depthStencilBuffer.getBufferFormat().getInternalFormat();
		checkState(depthStencilInternalFormat == GL_DEPTH_COMPONENT
				|| depthStencilInternalFormat == GL_DEPTH_STENCIL);

		if (depthStencilInternalFormat == GL_DEPTH_COMPONENT) {
			getGl().glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, 
					GL_RENDERBUFFER, depthStencilBuffer.getGlResourceHandle());
			checkError();
		} else if (depthStencilInternalFormat == GL_DEPTH_STENCIL) {
			getGl().glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, 
					GL_RENDERBUFFER, depthStencilBuffer.getGlResourceHandle());
			checkError();
			getGl().glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_STENCIL_ATTACHMENT, 
					GL_RENDERBUFFER, depthStencilBuffer.getGlResourceHandle());
			checkError();
		}

		int fbStatus = getGl().glCheckFramebufferStatus(GL_FRAMEBUFFER);
		checkError();
		checkState(fbStatus == GL_FRAMEBUFFER_COMPLETE, 
				resourceMsg("Unable to initialize Framebuffer [%s]"), 
				Integer.toHexString(fbStatus));
		unbind();

		if (log.isDebugEnabled()) {
			log.debug(resourceMsg("Framebuffer attachments initialized."));
		}
	}

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
		getGl().glBindFramebuffer(getBindMode(), getGlResourceHandle());
	}

	@Override
	protected void doUnbind() {
		getGl().glBindFramebuffer(getBindMode(), ZERO);
	}

	@Override
	protected void doDestroy() {
		getGl().glDeleteFramebuffers(ONE, intBuffer(getGlResourceHandle()));
	}

	public Map<Integer, GLTexture2D> getColorAttachments() { return colorAttachments; }
	public Map<Integer, Integer> getColorAttachmentParameters() { return colorAttachmentParameters; }
	public GLTextureMetadata getColorAttachmentFormat() { return colorAttachmentFormat; }
	public GLRenderBuffer getDepthStencilBuffer() { return depthStencilBuffer; }
	public boolean isAttachmentsInitialized() { return attachmentsInitialized; }

	public int getBindMode() { 
		checkState(getBindMode() != MINUS_ONE);
		return bindMode; 
	}

	public void setBindMode(int bindMode) {
		checkArgument(GL_FRAMEBUFFER_TARGET.contains(bindMode));
		this.bindMode = bindMode;
	}
}