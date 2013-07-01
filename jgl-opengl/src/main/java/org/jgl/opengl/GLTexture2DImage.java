package org.jgl.opengl;

import java.nio.*;

public class GLTexture2DImage {

	protected Buffer imageData;
	private final GLTextureMetadata metadata = new GLTextureMetadata();
	
	public Buffer getImageData() { return imageData; }
	public void setImageData(Buffer imageData) { this.imageData = imageData; }
	public GLTextureMetadata getMetadata() { return metadata; }
}
