package org.jgl.opengl;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static com.google.common.base.Preconditions.*;

public class GLBufferMetadata {

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

	public int getTotalComponentByteSize() {
		
		int byteSum = 0;
		
		for (int k = 0; k < componentUnitSizes.length; k++) {
			byteSum += getComponentByteSize(k);
		}
		return byteSum;
	}	
	
	public int getByteSizeof(int glType) {
		switch (glType) {
			case GL_BYTE:           return 1;
			case GL_UNSIGNED_BYTE:  return 1;
			case GL_SHORT:          return 2;
			case GL_UNSIGNED_SHORT: return 2;
			case GL_INT:            return 4;
			case GL_UNSIGNED_INT:   return 4;
			case GL_FLOAT:          return 4;
			case GL_DOUBLE:         return 8;
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
