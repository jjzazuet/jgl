package net.tribe7.opengl;

import static javax.media.opengl.GL.*;

public class GLTextureCubeMap extends GLTexture {

	@Override
	public int getTextureTarget() { return GL_TEXTURE_CUBE_MAP; }
	
	public int getCubeMapFaceTarget(int face) {
		return 0;
	}
}
