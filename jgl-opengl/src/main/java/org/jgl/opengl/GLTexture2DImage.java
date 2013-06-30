package org.jgl.opengl;

import static com.google.common.base.Preconditions.*;
import java.nio.ByteBuffer;

public class GLTexture2DImage {

	private final ByteBuffer imageData;
	private final GLTextureMetadata metadata = new GLTextureMetadata();
	
	public GLTexture2DImage(ByteBuffer imageData) {
		checkNotNull(imageData);
		checkArgument(imageData.capacity() > 0);
		this.imageData = imageData;
	}

	public ByteBuffer getImageData() { return imageData; }
	public GLTextureMetadata getMetadata() { return metadata; }
}
