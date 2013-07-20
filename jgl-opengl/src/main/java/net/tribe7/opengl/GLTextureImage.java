package net.tribe7.opengl;

import java.nio.*;

public class GLTextureImage {

	private Buffer imageData;
	private final GLTextureMetadata metadata = new GLTextureMetadata();

	public Buffer getImageData() { return imageData; }
	public void setImageData(Buffer imageData) { this.imageData = imageData; }
	public GLTextureMetadata getMetadata() { return metadata; }

	@Override
	public String toString() {
		return String.format("%s [buffer:%s, metadata:%s]", 
				getClass().getSimpleName(),
				getImageData(), getMetadata());
	}
}
