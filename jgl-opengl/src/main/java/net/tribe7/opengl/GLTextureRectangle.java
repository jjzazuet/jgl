package net.tribe7.opengl;

import static javax.media.opengl.GL2.*;

public class GLTextureRectangle extends GLTexture2D {
	@Override
	public int getTextureTarget() { return GL_TEXTURE_RECTANGLE; }
}
