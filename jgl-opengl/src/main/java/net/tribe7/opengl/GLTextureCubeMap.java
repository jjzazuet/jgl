package net.tribe7.opengl;

import static javax.media.opengl.GL.*;
import static net.tribe7.common.base.Preconditions.*;

public class GLTextureCubeMap extends GLTexture {

	public static final int SIX = 6;

	private final GLTextureImage positiveX = new GLTextureImage();
	private final GLTextureImage positiveY = new GLTextureImage();
	private final GLTextureImage positiveZ = new GLTextureImage();

	private final GLTextureImage negativeX = new GLTextureImage();
	private final GLTextureImage negativeY = new GLTextureImage();
	private final GLTextureImage negativeZ = new GLTextureImage();

	private final GLTextureImage [] cubeFaces = new GLTextureImage[] {
			positiveX, negativeX,
			positiveY, negativeY,
			positiveZ, negativeZ
	};

	@Override
	public void doLoadData() {
		for (int k = 0; k < cubeFaces.length; k++) {
			loadImage(cubeFaces[k], getCubeMapFaceTarget(k));
		}
	}

	@Override
	public int getTextureTarget() { return GL_TEXTURE_CUBE_MAP; }

	public int getCubeMapFaceTarget(int face) {
		checkElementIndex(face, SIX);
		return GL_TEXTURE_CUBE_MAP_POSITIVE_X + face;
	}

	public GLTextureImage getPositiveX() { return positiveX; }
	public GLTextureImage getPositiveY() { return positiveY; }
	public GLTextureImage getPositiveZ() { return positiveZ; }
	public GLTextureImage getNegativeX() { return negativeX; }
	public GLTextureImage getNegativeY() { return negativeY; }
	public GLTextureImage getNegativeZ() { return negativeZ; }
	public GLTextureImage[] getCubeFaces() { return cubeFaces; }
}
