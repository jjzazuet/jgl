package net.tribe7.math.matrix.io;

import java.nio.FloatBuffer;
import net.tribe7.math.matrix.Matrix4;

public class BufferedMatrix4 extends Matrix4 {

	private final FloatBuffer backingBuffer;
	
	public BufferedMatrix4() {
		backingBuffer = FloatBuffer.allocate(COMPONENT_SIZE);
	}

	public FloatBuffer getBackingBuffer() { return backingBuffer; }
}