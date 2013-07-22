package net.tribe7.opengl;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.common.base.Preconditions.*;

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

		checkBound();
		checkState(getDepthStencilBuffer().getBufferFormat().getWidth() ==
				getColorAttachment().getImage().getMetadata().getWidth());
		checkState(getDepthStencilBuffer().getBufferFormat().getHeight() ==
				getColorAttachment().getImage().getMetadata().getHeight());

		getColorAttachment().init(getGl());
		getColorAttachment().bind(); {
			getColorAttachment().loadData();
			getColorAttachment().applyParameters();
			setColorAttachment(GL_COLOR_ATTACHMENT0, getColorAttachment());
		} getColorAttachment().unbind();

		depthStencilBuffer.init(getGl());
		depthStencilBuffer.bind();
		depthStencilBuffer.initStorage();

		int depthStencilInternalFormat = depthStencilBuffer.getBufferFormat().getInternalFormat();
		checkState(isValidDepthStencilFormat(depthStencilInternalFormat));

		switch (depthStencilInternalFormat) {
		case GL_DEPTH_COMPONENT:
			setRenderBuffer(GL_DEPTH_ATTACHMENT, getDepthStencilBuffer());
			break;
		case GL_DEPTH_STENCIL:
			setRenderBuffer(GL_DEPTH_ATTACHMENT, getDepthStencilBuffer());
			setRenderBuffer(GL_STENCIL_ATTACHMENT, getDepthStencilBuffer());
			break;
		default: throw new IllegalArgumentException(
				resourceMsg(Integer.toHexString(depthStencilInternalFormat)));
		}
		checkError();
		depthStencilBuffer.unbind();
	}

	public GLTexture2D getColorAttachment() { return colorAttachment; }
	public GLRenderBuffer getDepthStencilBuffer() { return depthStencilBuffer; }
}
