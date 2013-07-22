package net.tribe7.opengl;

import static net.tribe7.common.base.Preconditions.*;
import java.nio.*;

public class GLTextureImage {

	private Buffer imageData;
	private final GLTextureMetadata metadata = new GLTextureMetadata();

	public Buffer getImageData() { return imageData; }
	public void setImageData(Buffer imageData) { this.imageData = imageData; }
	public GLTextureMetadata getMetadata() { return metadata; }

	public void setFrom(GLTextureImage other) {
		checkNotNull(other);
		checkArgument(this != other);
		setImageData(other.getImageData());
		getMetadata().setFrom(other.getMetadata());
	}

	@Override
	public String toString() {
		return String.format("%s [buffer:%s, metadata:%s]", 
				getClass().getSimpleName(),
				getImageData(), getMetadata());
	}
}
