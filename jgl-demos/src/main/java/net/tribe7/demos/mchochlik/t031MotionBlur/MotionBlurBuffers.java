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

	public MotionBlurBuffers() {
		setBindTarget(GL_DRAW_FRAMEBUFFER);
	}

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

		color1.init(getGl());
		color1.bind();
		color1.applyParameters();
		color1.unbind();
		rbo.getBufferFormat().setWidth(WIDTH);
		rbo.getBufferFormat().setHeight(HEIGHT);
		rbo.getBufferFormat().setInternalFormat(GL_DEPTH_COMPONENT);

		attach(GL_COLOR_ATTACHMENT0, color0);
		attach(rbo);
	}

	public void accumulate() {
		color1.setActive();
		color1.bind(); {
			getGl().glCopyTexImage2D(
					color1.getTextureTarget(), 0, 
					color1.getImage().getMetadata().getInternalFormat(), 
					0, 0, WIDTH, HEIGHT, 0);
			checkError();
		} color1.unbind();
	}

	public GLTexture2D getColor0() { return color0; }
	public GLTexture2D getColor1() { return color1; }
}
