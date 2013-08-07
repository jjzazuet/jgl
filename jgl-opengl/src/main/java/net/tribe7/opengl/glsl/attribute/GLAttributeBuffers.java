package net.tribe7.opengl.glsl.attribute;

import static net.tribe7.common.base.Charsets.UTF_8;
import static net.tribe7.common.base.Preconditions.*;

import java.nio.*;

public class GLAttributeBuffers {

	public static final int MINUS_ONE = -1;
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final String LEFT_BRACKET = "[";

	private final int attributeIndex;
	private final IntBuffer length = IntBuffer.allocate(ONE);
	private final IntBuffer size = IntBuffer.allocate(ONE);
	private final IntBuffer type = IntBuffer.allocate(ONE);
	private final ByteBuffer name;

	public GLAttributeBuffers(int attributeIndex, int nameLength) {
		checkArgument(attributeIndex > -ONE);
		checkArgument(nameLength >= ONE);
		this.name = ByteBuffer.allocate(nameLength + ONE);
		this.attributeIndex = attributeIndex;
	}

	public int getLengthValue() { 
		getLength().rewind(); 
		return getLength().get(); 
	}

	public int getSizeValue() { 
		getSize().rewind(); 
		return getSize().get(); 
	}

	public int getTypeValue() {
		getType().rewind();
		return getType().get();
	}

	public String getNameValue() {
		getName().rewind();
		String name = UTF_8.decode(getName()).toString().trim();
		if (name.contains(LEFT_BRACKET)) {
			name = name.substring(0, name.indexOf(LEFT_BRACKET));
		}
		return name;
	}

	public void clearBuffers() {
		getLength().clear();
		getSize().clear();
		getType().clear();
		getName().clear();
	}

	public int getAttributeIndex() { return attributeIndex; }
	public IntBuffer getLength() { return length; }
	public IntBuffer getSize() { return size; }
	public IntBuffer getType() { return type; }
	public ByteBuffer getName() { return name; }

	@Override
	public String toString() {
		return String.format("%s[index: %s, length: %s, size: %s, type: %s, name: %s]",
				getClass().getSimpleName(), getAttributeIndex(), 
				getLength(), getSize(), 
				getType(), getName());
	}
}
