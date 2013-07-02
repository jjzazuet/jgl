package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;

/**
 * <a href="http://www.opengl.org/sdk/docs/man/xhtml/glTexImage2D.xml">glTexImage2D</a>
 * @author jjzazuet
 */
public class GLTextureMetadata {

	public static final int ZERO = 0;
	public static final int MINUS_ONE = -1;

	private int width = MINUS_ONE;
	private int height = MINUS_ONE;
	private int internalFormat = MINUS_ONE;
	private int pixelDataFormat = MINUS_ONE;
	private int pixelDataType = MINUS_ONE;

	public int getWidth() { return width; }
	public void setWidth(int width) {
		checkArgument(width > ZERO);
		this.width = width;
	}

	public int getHeight() { return height; }
	public void setHeight(int height) {
		checkArgument(height > ZERO);
		this.height = height;
	}

	public int getInternalFormat() { return internalFormat; }
	public void setInternalFormat(int internalFormat) {
		boolean baseFormat = GLConstants.GL_TEXTURE_BASE_INTERNAL_FORMAT.contains(internalFormat);
		boolean sizedFormat = GLConstants.GL_TEXTURE_SIZED_INTERNAL_FORMAT.contains(internalFormat);
		boolean compressedFormat = GLConstants.GL_TEXTURE_COMPRESSED_INTERNAL_FORMAT.contains(internalFormat);
		checkArgument(baseFormat || sizedFormat || compressedFormat);
		this.internalFormat = internalFormat;
	}

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
