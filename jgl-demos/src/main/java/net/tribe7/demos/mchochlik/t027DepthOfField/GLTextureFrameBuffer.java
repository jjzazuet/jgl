package net.tribe7.demos.mchochlik.t027DepthOfField;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import net.tribe7.opengl.GLFrameBuffer;
import net.tribe7.opengl.GLTexture2D;
import net.tribe7.opengl.GLTextureRectangle;

public class GLTextureFrameBuffer extends GLFrameBuffer {

	public static final int WIDHT = 4096;
	public static final int HEIGHT = 4096;

	private final GLTextureRectangle colorAttachment = new GLTextureRectangle();
	private final GLTextureRectangle depthAttachment = new GLTextureRectangle();

	@Override
	protected void doInitAttachments() {

		colorAttachment.getImage().getMetadata().checkSize(
				depthAttachment.getImage().getMetadata());

		colorAttachment.init(getGl());
		depthAttachment.init(getGl());

		colorAttachment.bind(); {
			colorAttachment.getParameters().put(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			colorAttachment.getParameters().put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			colorAttachment.getParameters().put(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			colorAttachment.getParameters().put(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

			colorAttachment.getImage().getMetadata().setWidth(WIDHT);
			colorAttachment.getImage().getMetadata().setHeight(HEIGHT);
			colorAttachment.getImage().getMetadata().setInternalFormat(GL_RGB);
			colorAttachment.getImage().getMetadata().setPixelDataFormat(GL_RGB);
			colorAttachment.getImage().getMetadata().setPixelDataType(GL_UNSIGNED_BYTE);

			colorAttachment.loadData();
			colorAttachment.applyParameters();
			setAttachment(GL_COLOR_ATTACHMENT0, colorAttachment);
		} colorAttachment.unbind();

		depthAttachment.bind(); {
			depthAttachment.getParameters().put(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			depthAttachment.getParameters().put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			depthAttachment.getParameters().put(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			depthAttachment.getParameters().put(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

			depthAttachment.getImage().getMetadata().setWidth(WIDHT);
			depthAttachment.getImage().getMetadata().setHeight(HEIGHT);
			depthAttachment.getImage().getMetadata().setInternalFormat(GL_DEPTH_COMPONENT);
			depthAttachment.getImage().getMetadata().setPixelDataFormat(GL_DEPTH_COMPONENT);
			depthAttachment.getImage().getMetadata().setPixelDataType(GL_FLOAT);

			depthAttachment.loadData();
			depthAttachment.applyParameters();
			setAttachment(GL_DEPTH_ATTACHMENT, depthAttachment);
		} depthAttachment.unbind();
	}

	public GLTexture2D getColorAttachment() { return colorAttachment; }
	public GLTexture2D getDepthAttachment() { return depthAttachment; }
}
