package net.tribe7.demos.mchochlik.t031MotionBlur;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import net.tribe7.opengl.*;

public class MotionBlurBuffers extends GLFrameBuffer {

	public static final int WIDTH = 4096;
	public static final int HEIGHT = 4096;

	private final GLTexture2D color0 = new GLTexture2D();
	private final GLTexture2D color1 = new GLTexture2D();
	private final GLRenderBuffer rbo = new GLRenderBuffer();
	private final GLTexture2D [] colorTargets = new GLTexture2D [] { color0, color1 };

	@Override
	protected void doInitAttachments() {

		for (GLTexture2D t : colorTargets) {
			t.getImage().getMetadata().setWidth(WIDTH);
			t.getImage().getMetadata().setHeight(HEIGHT);
			t.getImage().getMetadata().setInternalFormat(GL_RGB);
			t.getImage().getMetadata().setPixelDataFormat(GL_RGB);
			t.getImage().getMetadata().setPixelDataType(GL_UNSIGNED_BYTE);
			t.getParameters().put(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			t.getParameters().put(GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			t.getParameters().put(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			t.getParameters().put(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		}

		rbo.getBufferFormat().setWidth(WIDTH);
		rbo.getBufferFormat().setHeight(HEIGHT);
		rbo.getBufferFormat().setInternalFormat(GL_DEPTH_COMPONENT);

		attach(GL_COLOR_ATTACHMENT0, color0);
		attach(rbo);
	}
}
