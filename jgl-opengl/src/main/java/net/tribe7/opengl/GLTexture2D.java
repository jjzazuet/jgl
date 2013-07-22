package net.tribe7.opengl;

import static javax.media.opengl.GL.*;

public class GLTexture2D extends GLTexture {

	private final GLTextureImage image = new GLTextureImage();

	@Override
	public void doLoadData() {
		super.loadImage(image, getTextureTarget());
	}

	public GLTextureImage getImage() { return image; }

	@Override
	public int getTextureTarget() { return GL_TEXTURE_2D; }

	@Override
	public String toString() {
		return String.format("%s [image: %s]", super.toString(), getImage());
	}
}
