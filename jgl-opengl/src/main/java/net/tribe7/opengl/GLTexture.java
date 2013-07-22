package net.tribe7.opengl;

import static java.lang.String.format;
import static javax.media.opengl.GL.GL_TEXTURE0;
import static javax.media.opengl.GL.GL_TEXTURE_BINDING_2D;
import static javax.media.opengl.GL2ES2.GL_MAX_TEXTURE_IMAGE_UNITS;
import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.opengl.GLConstants.*;
import static net.tribe7.opengl.util.GLBufferUtils.intBuffer;

import java.nio.IntBuffer;
import java.util.Random;

public abstract class GLTexture extends GLContextBoundResource {

	private int textureUnit = MINUS_ONE;

	public abstract int getTextureTarget();

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
		getGl().glBindTexture(getTextureTarget(), getGlResourceHandle());
	}

	@Override
	protected void doUnbind() {
		getGl().glBindTexture(getTextureTarget(), ZERO);
	}

	@Override
	protected void doDestroy() {
		getGl().glDeleteTextures(ONE, intBuffer(getGlResourceHandle()));
	}

	protected void loadData(GLTextureImage image, int textureImageTarget) {
		checkBound();
		checkNotNull(image);
		getGl().glTexImage2D(textureImageTarget, 
				image.getMetadata().getMipMapLevel(),
				image.getMetadata().getInternalFormat(),
				image.getMetadata().getWidth(),
				image.getMetadata().getHeight(), ZERO,
				image.getMetadata().getPixelDataFormat(),
				image.getMetadata().getPixelDataType(),
				image.getImageData());
		checkError();
	}

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

	public void generateMipMap() {
		checkBound();
		getGl().glGenerateMipmap(getTextureTarget());
		checkError();
	}

	public int getTextureUnit() { return textureUnit; }
	protected void setTextureUnit(int textureUnit) { 
		checkArgument(textureUnit > MINUS_ONE);
		this.textureUnit = textureUnit; 
	}

	public int getTextureUnitEnum() { 
		checkState(textureUnit != MINUS_ONE);
		return GL_TEXTURE0 + textureUnit; 
	}

	protected void logActiveTexture() {
		if (log.isDebugEnabled()) {
			IntBuffer ib = IntBuffer.wrap(new int[1]);
			getGl().glGetIntegerv(GL_TEXTURE_BINDING_2D, ib);
			log.debug(format("%s [%s]", resourceMsg("Bound texture"), ib.get()));
		}
	}

	@Override
	public String toString() {
		return String.format("%s [target: %s, textureUnit: %s, glTextureUnit: %s]",
				super.toString(), 
				Integer.toHexString(getTextureTarget()),
				getTextureUnit(),
				Integer.toHexString(getTextureUnitEnum()));
	}
}
