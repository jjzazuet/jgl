package org.jgl.opengl;

import static java.lang.String.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static com.google.common.base.Preconditions.*;
import static org.jgl.opengl.util.GLBufferUtils.*;
import static org.jgl.opengl.GLConstants.*;
import java.nio.IntBuffer;
import java.util.Random;

public class GLTexture2D extends GLContextBoundResource {

	private final int textureTarget = GL_TEXTURE_2D;
	private int textureUnit = MINUS_ONE;
	private GLTexture2DImage image = null;

	public void setParameter(int parameter, float value) {
		checkArgument(GL_TEXTURE_PARAMETER.contains(parameter));
		bind();
		getGl().glTexParameterf(getTextureTarget(), parameter, value);
		checkError();
		unbind();
	}

	public void setParameter(int parameter, int value) {
		checkArgument(GL_TEXTURE_PARAMETER.contains(parameter));
		bind();
		getGl().glTexParameteri(getTextureTarget(), parameter, value);
		checkError();
		unbind();
	}
	
	public void loadData(GLTexture2DImage image) {

		checkInitialized();
		checkState(this.image == null, "Image data already loaded!");
		this.image = image;

		bind();
		getGl().glTexImage2D(getTextureTarget(), ZERO, 
				getImage().getMetadata().getInternalFormat(), 
				getImage().getMetadata().getWidth(), 
				getImage().getMetadata().getHeight(), ZERO, 
				getImage().getMetadata().getPixelDataFormat(), 
				getImage().getMetadata().getPixelDataType(), 
				getImage().getImageData());
		checkError();
		unbind();
	}

	@Override
	protected void doInit() {

		IntBuffer i1 = IntBuffer.allocate(ONE);
		getGl().glGenTextures(ONE, i1);
		checkError();
		setGlResourceHandle(i1.get());

		i1.clear();
		getGl().glGetIntegerv(GL_MAX_TEXTURE_IMAGE_UNITS, i1);
		checkError();
		int maxTextureUnits = i1.get();
		int textureUnit = new Random().nextInt(maxTextureUnits - ZERO + 1) + ZERO;
		setTextureUnit(textureUnit);
	}

	@Override
	protected void doBind() {
		logActiveTexture();
		getGl().glBindTexture(getTextureTarget(), getGlResourceHandle());
	}

	@Override
	protected void doUnbind() {
		logActiveTexture();
		getGl().glBindTexture(getTextureTarget(), ZERO);
	}

	@Override
	protected void doDestroy() {
		getGl().glDeleteTextures(ONE, intBuffer(getGlResourceHandle()));
	}

	protected void logActiveTexture() {
		if (log.isDebugEnabled()) {
			IntBuffer ib = IntBuffer.wrap(new int[1]);
			getGl().glGetIntegerv(GL_TEXTURE_BINDING_2D, ib);
			log.debug(format("%s [%s]", resourceMsg("Bound texture"), ib.get()));
		}
	}

	public void generateMipMap() {
		bind();
		getGl().glGenerateMipmap(getTextureTarget());
		checkError();
		unbind();
	}

	public GLTexture2DImage getImage() { return image; }

	public int getTextureTarget() { return textureTarget; }
	public int getTextureUnitEnum() { 
		checkState(textureUnit != MINUS_ONE);
		return GL_TEXTURE0 + textureUnit; 
	}

	public int getTextureUnit() { return textureUnit; }
	protected void setTextureUnit(int textureUnit) { 
		checkArgument(textureUnit > MINUS_ONE);
		this.textureUnit = textureUnit; 
	}

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
