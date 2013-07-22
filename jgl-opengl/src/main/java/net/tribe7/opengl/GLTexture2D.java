package net.tribe7.opengl;

import static javax.media.opengl.GL.*;
import static net.tribe7.common.base.Preconditions.*;

public class GLTexture2D extends GLTexture {

	private GLTextureImage image = null;

	public void loadData(GLTextureImage image) {
		checkState(this.image == null, "Image data already loaded!");
		super.loadData(image, getTextureTarget());
		this.image = image;
	}

	public GLTextureImage getImage() { return image; }

	@Override
	public int getTextureTarget() { return GL_TEXTURE_2D; }

	@Override
	public String toString() {
		return String.format("%s [image: %s]", super.toString(), getImage());
	}
}
