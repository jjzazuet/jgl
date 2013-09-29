package net.tribe7.opengl;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.common.base.Preconditions.*;

public class GLBufferMetadata {

	public static final int GL_BYTE_SIZE           = 1;
	public static final int GL_UNSIGNED_BYTE_SIZE  = 1;
	public static final int GL_SHORT_SIZE          = 2;
	public static final int GL_UNSIGNED_SHORT_SIZE = 2;
	public static final int GL_INT_SIZE            = 4;
	public static final int GL_UNSIGNED_INT_SIZE   = 4;
	public static final int GL_FLOAT_SIZE          = 4;
	public static final int GL_DOUBLE_SIZE         = 8;
	
	private final int glPrimitiveType;
	private final int[] componentUnitSizes;

	public GLBufferMetadata(int[] componentUnitSizes, int glPrimitiveType) {
		this.componentUnitSizes = checkNotNull(componentUnitSizes);
		checkArgument(componentUnitSizes.length > 0);
		checkArgument(isValidPrimitiveType(glPrimitiveType));
		this.glPrimitiveType = glPrimitiveType;
	}

	public int getTotalComponentUnitSize() {
		int unitSum = 0;
		for (int k = 0; k < componentUnitSizes.length; k++) {
			unitSum += getComponentUnitSize(k);
		}
		return unitSum;
	}
	
	public int getComponentUnitSize(int componentIndex) {
		componentIndex = checkPositionIndex(componentIndex, componentUnitSizes.length);
		int componentSize = componentUnitSizes[componentIndex];
		checkArgument(componentSize > 0);
		return componentSize;
	}
	
	public int getTotalComponentByteSize() {
		int byteSum = 0;
		for (int k = 0; k < componentUnitSizes.length; k++) {
			byteSum += getComponentByteSize(k);
		}
		return byteSum;
	}

	public int getComponentByteSize(int componentIndex) {
		return getByteSizeof(glPrimitiveType) * getComponentUnitSize(componentIndex); 
	}

	public int getComponentByteOffset(int componentIndex) {
		int byteOffset = 0;
		for (int k = 0; k < componentIndex; k++) {
			byteOffset += getComponentByteSize(k);
		}
		return byteOffset;
	}

	public int getByteSizeof(int glType) {
		switch (glType) {
			case GL_BYTE:           return GL_BYTE_SIZE;
			case GL_UNSIGNED_BYTE:  return GL_UNSIGNED_BYTE_SIZE;
			case GL_SHORT:          return GL_SHORT_SIZE;
			case GL_UNSIGNED_SHORT: return GL_UNSIGNED_SHORT_SIZE;
			case GL_INT:            return GL_INT_SIZE;
			case GL_UNSIGNED_INT:   return GL_UNSIGNED_INT_SIZE;
			case GL_FLOAT:          return GL_FLOAT_SIZE;
			case GL_DOUBLE:         return GL_DOUBLE_SIZE;
		}
		return -1;
	}

	protected boolean isValidPrimitiveType(int glType) {
		return getByteSizeof(glType) != -1;
	}

	public int[] getComponentSizes() { return componentUnitSizes; }
	public int getComponentCount() { return componentUnitSizes.length; }
	public int getGlPrimitiveType() { return glPrimitiveType; }
}
