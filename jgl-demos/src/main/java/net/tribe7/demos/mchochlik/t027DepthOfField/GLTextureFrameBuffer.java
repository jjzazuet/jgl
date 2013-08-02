package net.tribe7.demos.mchochlik.t027DepthOfField;

import static javax.media.opengl.GL.*;
import net.tribe7.opengl.GLFrameBuffer;
import net.tribe7.opengl.GLTexture2D;

public class GLTextureFrameBuffer extends GLFrameBuffer {

	private final GLTexture2D colorAttachment = new GLTexture2D();
	private final GLTexture2D depthAttachment = new GLTexture2D();

	@Override
	protected void doInitAttachments() {

		getColorAttachment().getImage().getMetadata().checkSize(
				getDepthAttachment().getImage().getMetadata());

		getColorAttachment().init(getGl());
		getDepthAttachment().init(getGl());

		getColorAttachment().bind(); {
			getColorAttachment().loadData();
			getColorAttachment().applyParameters();
			setAttachment(GL_COLOR_ATTACHMENT0, getColorAttachment());
		} getColorAttachment().unbind();

		getDepthAttachment().bind(); {
			getDepthAttachment().loadData();
			getDepthAttachment().applyParameters();
			setAttachment(GL_DEPTH_ATTACHMENT, getDepthAttachment());
		} getDepthAttachment().unbind();
	}

	public GLTexture2D getColorAttachment() { return colorAttachment; }
	public GLTexture2D getDepthAttachment() { return depthAttachment; }
}
