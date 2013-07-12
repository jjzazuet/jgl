package net.tribe7.math.matrix.io;

import java.nio.FloatBuffer;

import net.tribe7.math.matrix.Matrix2;

public class BufferedMatrix2 extends Matrix2 {

	private final FloatBuffer backingBuffer;

	public BufferedMatrix2() {
		backingBuffer = FloatBuffer.allocate(COMPONENT_SIZE);
	}
	public FloatBuffer getBackingBuffer() {
		return backingBuffer;
	}
}
