package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;

public class GLImageMetadata {

	public static final int ZERO = 0;
	public static final int MINUS_ONE = -1;

	private int width = MINUS_ONE;
	private int height = MINUS_ONE;
	private int internalFormat = MINUS_ONE;

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

	public void setFrom(GLImageMetadata reference) {
		checkNotNull(reference);
		setWidth(reference.getWidth());
		setHeight(reference.getHeight());
		setInternalFormat(reference.getInternalFormat());
	}
}