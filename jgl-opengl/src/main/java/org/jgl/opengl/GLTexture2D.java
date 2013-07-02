package org.jgl.opengl;

import static javax.media.opengl.GL.*;
import static com.google.common.base.Preconditions.*;
import static org.jgl.opengl.util.GLBufferUtils.*;
import static org.jgl.opengl.GLConstants.*;
import java.nio.IntBuffer;

public class GLTexture2D extends GLContextBoundResource {

	private int textureTarget = MINUS_ONE;
	private int textureUnit = MINUS_ONE;
	private GLTexture2DImage image = null;

	public void setParameter(int parameter, float value) {
		checkBound();
		checkArgument(GL_TEXTURE_PARAMETER.contains(parameter));
		getGl().glTexParameterf(getTextureTarget(), parameter, value);
		checkError();
	}
	
	public void setParameter(int parameter, int value) {
		checkBound();
		checkArgument(GL_TEXTURE_PARAMETER.contains(parameter));
		getGl().glTexParameteri(getTextureTarget(), parameter, value);
		checkError();
	}
	
	public void loadData(GLTexture2DImage image) {
		
		checkInitialized();
		checkBound();
		checkState(this.image == null, "Image data already loaded!");
		this.image = image;
		
		getGl().glTexImage2D(getTextureTarget(), ZERO, 
				getImage().getMetadata().getInternalFormat(), 
				getImage().getMetadata().getWidth(), 
				getImage().getMetadata().getHeight(), ZERO, 
				getImage().getMetadata().getPixelDataFormat(), 
				getImage().getMetadata().getPixelDataType(), 
				getImage().getImageData());
		checkError();
	}
	@Override
	protected void doInit() {
		IntBuffer i1 = IntBuffer.allocate(ONE);
		getGl().glGenTextures(ONE, i1);
		setGlResourceHandle(i1.get());
		checkError();
	}

	@Override
	protected void doBind() {
		checkState(getTextureTarget() != MINUS_ONE);
		checkState(getTextureUnitEnum() != MINUS_ONE);
		getGl().glActiveTexture(getTextureUnitEnum());
		getGl().glBindTexture(getTextureTarget(), getGlResourceHandle());
		checkError();
	}

	@Override
	protected void doUnbind() {
		getGl().glActiveTexture(getTextureUnitEnum());
		getGl().glBindTexture(getTextureTarget(), ZERO);
		checkError();
	}

	@Override
	protected void doDestroy() {
		getGl().glDeleteTextures(ONE, intBuffer(getGlResourceHandle()));
		checkError();
	}

	public void generateMipMap() {
		checkBound();
		getGl().glGenerateMipmap(getTextureTarget());
		checkError();
	}

	public GLTexture2DImage getImage() { return image; }
	public int getTextureTarget() { return textureTarget; }
	public void setTextureTarget(int textureTarget) {
		checkArgument(GL_TEXTURE_TARGET.contains(textureTarget));
		this.textureTarget = textureTarget; 
	}

	public int getTextureUnitEnum() { return GL_TEXTURE0 + textureUnit; }
	public int getTextureUnit() { return textureUnit; }
	public void setTextureUnit(int textureUnit) { this.textureUnit = textureUnit; }
	
	@Override
	public String toString() {
		return String.format("%s [target: %s, textureUnit: %s, glTextureUnit: %s, image:%s]",
				super.toString(), 
				Integer.toHexString(getTextureTarget()),
				getTextureUnit(),
				Integer.toHexString(getTextureUnitEnum()),
				getImage());
	}
}
