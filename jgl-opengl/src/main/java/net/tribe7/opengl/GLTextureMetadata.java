package net.tribe7.opengl;

import static net.tribe7.common.base.Preconditions.*;

/**
 * <a href="http://www.opengl.org/sdk/docs/man/xhtml/glTexImage2D.xml">glTexImage2D</a>
 * @author jjzazuet
 */
public class GLTextureMetadata extends GLImageMetadata {

	private int pixelDataFormat = MINUS_ONE;
	private int pixelDataType = MINUS_ONE;

	public int getPixelDataFormat() { return pixelDataFormat; }
	public void setPixelDataFormat(int format) {
		checkArgument(GLConstants.GLTEXTURE_PIXEL_DATA_FORMAT.contains(format));
		this.pixelDataFormat = format;
	}

	public int getPixelDataType() { return pixelDataType; }
	public void setPixelDataType(int type) {
		checkArgument(GLConstants.GL_TEXTURE_PIXEL_DATA_TYPE.contains(type));
		this.pixelDataType = type;
	}

	public void setFrom(GLTextureMetadata reference) {
		super.setFrom(reference);
		setPixelDataFormat(reference.getPixelDataFormat());
		setPixelDataType(reference.getPixelDataType());
	}

	@Override
	public String toString() {
		return String.format("%s [w:%s, h:%s, internalFormat:%s, pixelFormat:%s, pixelType:%s]", 
				getClass().getSimpleName(),
				getWidth(), getHeight(), 
				Integer.toHexString(getInternalFormat()), 
				Integer.toHexString(getPixelDataFormat()), 
				Integer.toHexString(getPixelDataType()));
	}
}
