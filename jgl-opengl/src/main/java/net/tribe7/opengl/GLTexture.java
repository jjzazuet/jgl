package net.tribe7.opengl;

import static java.lang.String.format;
import static javax.media.opengl.GL.GL_TEXTURE0;
import static javax.media.opengl.GL.GL_TEXTURE_BINDING_2D;
import static javax.media.opengl.GL2ES2.GL_MAX_TEXTURE_IMAGE_UNITS;
import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.opengl.GLConstants.*;
import static net.tribe7.opengl.util.GLBufferUtils.intBuffer;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public abstract class GLTexture extends GLContextBoundResource {

	private int textureUnit = MINUS_ONE;
	private boolean dataLoaded = false;
	private Map<Integer, Number> parameters = new HashMap<Integer, Number>();

	public abstract int getTextureTarget();
	public abstract void doLoadData();

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

	protected void loadImage(GLTextureImage image, int textureImageTarget) {
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

	public void loadData() {
		checkState(!isDataLoaded(), resourceMsg("Texture data already loaded."));
		doLoadData();
		setDataLoaded(true);
	}

	public void applyParameters() {
		checkBound();
		for (Entry<Integer, Number> parameter : getParameters().entrySet()) {
			checkArgument(GL_TEXTURE_PARAMETER.contains(parameter.getKey()));
			if (parameter.getValue() instanceof Integer) {
				getGl().glTexParameteri(getTextureTarget(), parameter.getKey(), (Integer) parameter.getValue());
			} else if (parameter.getValue() instanceof Float) {
				getGl().glTexParameterf(getTextureTarget(), parameter.getKey(), (Float) parameter.getValue());
			} else throw new IllegalArgumentException(parameter.getValue().getClass().getCanonicalName());
			checkError();
		}
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

	public Map<Integer, Number> getParameters() { return parameters; }

	protected void logActiveTexture() {
		if (log.isDebugEnabled()) {
			IntBuffer ib = IntBuffer.wrap(new int[1]);
			getGl().glGetIntegerv(GL_TEXTURE_BINDING_2D, ib);
			log.debug(format("%s [%s]", resourceMsg("Bound texture"), ib.get()));
		}
	}

	protected boolean isDataLoaded() { return dataLoaded; }
	protected void setDataLoaded(boolean dataLoaded) { this.dataLoaded = dataLoaded; }

	@Override
	public String toString() {
		return String.format("%s [target: %s, textureUnit: %s, glTextureUnit: %s]",
				super.toString(), 
				Integer.toHexString(getTextureTarget()),
				getTextureUnit(),
				Integer.toHexString(getTextureUnitEnum()));
	}
}
