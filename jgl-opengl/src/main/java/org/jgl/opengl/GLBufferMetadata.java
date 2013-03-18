package org.jgl.opengl;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static com.google.common.base.Preconditions.*;

public class GLBufferMetadata {

	private final int glPrimitiveType;
	private final int[] componentSizes;
	private int totalComponentSize = 0;

	public GLBufferMetadata(int[] componentSizes, int glPrimitiveType) {

		this.componentSizes = checkNotNull(componentSizes);
		
		checkArgument(componentSizes.length > 0);
		checkArgument(isValidPrimitiveType(glPrimitiveType));

		this.glPrimitiveType = glPrimitiveType;
		
		for (int k = 0; k < componentSizes.length; k++) {
			totalComponentSize += getComponentSize(k);
		}
	}

	public int getComponentSize(int componentIndex) {
		componentIndex = checkPositionIndex(componentIndex, componentSizes.length);
		int componentSize = componentSizes[componentIndex];
		checkArgument(componentSize > 0);
		return componentSize;
	}
	
	protected boolean isValidPrimitiveType(int glType) {
		return getByteSizeofPrimitive(glType) != -1;
	}

	public int getByteSizeofPrimitive(int glType) {
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

	public int getComponentByteSize(int componentIndex) {
		return getByteSizeofPrimitive(glPrimitiveType) * getComponentSize(componentIndex); 
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
		
		for (int k = 0; k < componentSizes.length; k++) {
			byteSum += getComponentByteSize(k);
		}
		return byteSum;
	}

	public int getTotalComponentSize() { return totalComponentSize; }
	public int[] getComponentSizes() { return componentSizes; }
	public int getComponentCount() { return componentSizes.length; }
	public int getGlPrimitiveType() { return glPrimitiveType; }
}
