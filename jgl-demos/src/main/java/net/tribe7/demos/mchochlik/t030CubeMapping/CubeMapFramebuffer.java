package net.tribe7.demos.mchochlik.t030CubeMapping;

import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.math.Preconditions.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import net.tribe7.opengl.*;

public class CubeMapFramebuffer extends GLFrameBuffer {

	private final GLTextureCubeMap cubeColorAttachment = new GLTextureCubeMap();
	private final GLTextureCubeMap cubeDepthAttachment = new GLTextureCubeMap();
	private final GLTextureCubeMap [] attachments = new GLTextureCubeMap[] { cubeColorAttachment, cubeDepthAttachment };
	private int width = MINUS_ONE, height = MINUS_ONE;

	public CubeMapFramebuffer(int width, int height) {
		checkArgument(width > MINUS_ONE);
		checkArgument(height > MINUS_ONE);
		this.width = width;
		this.height = height;
	}

	@Override
	protected void doInitAttachments() {

		for (GLTextureImage ci : cubeColorAttachment.getCubeFaces()) {
			ci.getMetadata().setInternalFormat(GL_RGB);
			ci.getMetadata().setWidth(width);
			ci.getMetadata().setHeight(height);
			ci.getMetadata().setPixelDataFormat(GL_RGB);
			ci.getMetadata().setPixelDataType(GL_UNSIGNED_BYTE);
		}

		for (GLTextureImage ci : cubeDepthAttachment.getCubeFaces()) {
			ci.getMetadata().setInternalFormat(GL_DEPTH_COMPONENT);
			ci.getMetadata().setWidth(width);
			ci.getMetadata().setHeight(height);
			ci.getMetadata().setPixelDataFormat(GL_DEPTH_COMPONENT);
			ci.getMetadata().setPixelDataType(GL_FLOAT);
		}

		for (GLTextureCubeMap cm : attachments) {
			cm.getParameters().put(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			cm.getParameters().put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			cm.getParameters().put(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			cm.getParameters().put(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			cm.getParameters().put(GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
		}

		attach(GL_COLOR_ATTACHMENT0, getCubeColorAttachment());
		attach(GL_DEPTH_ATTACHMENT, getCubeDepthAttachment());
	}

	public GLTextureCubeMap getCubeColorAttachment() { return cubeColorAttachment; }
	public GLTextureCubeMap getCubeDepthAttachment() { return cubeDepthAttachment; }
}
