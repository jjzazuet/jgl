package net.tribe7.opengl;

import static javax.media.opengl.GL.*;

/**
 * A Framebuffer Object with one {@link GLTexture2D} color attachment 
 * and one {@link GLRenderBuffer} depth/stencil attachment.
 * @author jjzazuet
 */
public class GLFrameBufferCompact extends GLFrameBuffer {

	private final GLTexture2D colorAttachment = new GLTexture2D();
	private final GLRenderBuffer depthStencilBuffer = new GLRenderBuffer();

	@Override
	protected void doInitAttachments() {
		getDepthStencilBuffer().getBufferFormat().checkSize(getColorAttachment().getImage().getMetadata());
		attach(GL_COLOR_ATTACHMENT0, colorAttachment);
		attach(getDepthStencilBuffer());
	}

	public GLTexture2D getColorAttachment() { return colorAttachment; }
	public GLRenderBuffer getDepthStencilBuffer() { return depthStencilBuffer; }
}
