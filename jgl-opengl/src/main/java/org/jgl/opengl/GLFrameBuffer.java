package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static org.jgl.opengl.util.GLBufferUtils.*;
import java.nio.IntBuffer;
import java.util.*;

public class GLFrameBuffer extends GLContextBoundResource {

	private boolean attachmentsInitialized = false;	
	private Map<Integer, GLTexture2D> colorAttachments = new HashMap<Integer, GLTexture2D>();
	private GLTextureMetadata colorAttachmentFormat = new GLTextureMetadata();
	private GLRenderBuffer depthStencilRenderBuffer = new GLRenderBuffer();

	public void initAttachments() {

		checkInitialized();
		GLTexture2DImage attachmentImage = new GLTexture2DImage();
		attachmentImage.getMetadata().setFrom(getColorAttachmentFormat());

		for (GLTexture2D colorAttachment : getColorAttachments().values()) {
			colorAttachment.init(getGl());
			colorAttachment.setTextureTarget(GL_TEXTURE_2D);
			colorAttachment.bind();
			colorAttachment.loadData(attachmentImage);
			colorAttachment.unbind();
			// attach a texture to FBO color attachement point
			// glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0);
		}

		depthStencilRenderBuffer.init(getGl()); // TODO finish init
		depthStencilRenderBuffer.initStorage();
		
		// attach a renderbuffer to depth attachment point
		// glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboId);
		// attach a renderbuffer to stencil attachment point
		// glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rboId);
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
		checkState(isAttachmentsInitialized(), resourceMsg("Color/Depth/Stencil attachments not initialized."));
		getGl().glBindFramebuffer(GL_FRAMEBUFFER, getGlResourceHandle());
	}

	@Override
	protected void doUnbind() {
		getGl().glBindFramebuffer(GL_FRAMEBUFFER, ZERO);
	}

	@Override
	protected void doDestroy() {
		getGl().glDeleteFramebuffers(ONE, intBuffer(getGlResourceHandle()));
	}

	public Map<Integer, GLTexture2D> getColorAttachments() { return colorAttachments; }
	public GLTextureMetadata getColorAttachmentFormat()    { return colorAttachmentFormat; }
	public GLRenderBuffer getDepthStencilRenderBuffer() { return depthStencilRenderBuffer; }
	public boolean isAttachmentsInitialized() { return attachmentsInitialized; }
}
