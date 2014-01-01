package net.tribe7.opengl;

import static net.tribe7.opengl.util.GLBufferUtils.*;
import static net.tribe7.common.base.Preconditions.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static com.jogamp.common.nio.Buffers.*;

import java.nio.*;
import javax.media.opengl.GL3;
import net.tribe7.geom.io.GeometryBuffer;

public class GLBufferFactory {

	public static GLBuffer buffer(GeometryBuffer<? extends Number> gb, GL3 gl, int glBufferType, int glUsageHint) {

		checkNotNull(gb);

		Number [] data = gb.getData();
		
		if (data instanceof Float[]) {
			return buffer(primitiveArray((Float[]) data), gl, glBufferType, glUsageHint, gb.getComponentSize());
		} else if (data instanceof Double[]) {
			return buffer(primitiveArray((Double[]) data), gl, glBufferType, glUsageHint, gb.getComponentSize());
		} else if (data instanceof Integer[]) {
			return buffer(primitiveArray((Integer[]) data), gl, glBufferType, glUsageHint, gb.getComponentSize());
		} else if (data instanceof Short[]) {
			return buffer(primitiveArray((Short[]) data), gl, glBufferType, glUsageHint, gb.getComponentSize());
		} else if (data instanceof Byte[]) {
			return buffer(primitiveArray((Byte[]) data), gl, glBufferType, glUsageHint, gb.getComponentSize());	
		} else {
			throw new IllegalArgumentException(
					String.format("Invalid geometry buffer type [%s]", 
							gb.getData().getClass().getCanonicalName()));
		}
	}

	public static GLBuffer buffer(float [] data, GL3 gl, int glBufferType, int glUsageHint, int componentSize) {
		return createBuffer(data, gl, glBufferType, glUsageHint, 
				new GLBufferMetadata(new int [] { componentSize }, GL_FLOAT));
	}

	public static GLBuffer buffer(double [] data, GL3 gl, int glBufferType, int glUsageHint, int componentSize) {
		return createBuffer(data, gl, glBufferType, glUsageHint, 
				new GLBufferMetadata(new int [] { componentSize }, GL_DOUBLE));
	}

	public static GLBuffer buffer(int [] data, GL3 gl, int glBufferType, int glUsageHint, int componentSize) {
		return createBuffer(data, gl, glBufferType, glUsageHint, 
				new GLBufferMetadata(new int [] { componentSize }, GL_UNSIGNED_INT));
	}

	public static GLBuffer buffer(short [] data, GL3 gl, int glBufferType, int glUsageHint, int componentSize) {
		return createBuffer(data, gl, glBufferType, glUsageHint, 
				new GLBufferMetadata(new int [] { componentSize }, GL_UNSIGNED_SHORT));
	}

	public static GLBuffer buffer(byte [] data, GL3 gl, int glBufferType, int glUsageHint, int componentSize) {
		return createBuffer(data, gl, glBufferType, glUsageHint, 
				new GLBufferMetadata(new int [] { componentSize }, GL_UNSIGNED_BYTE));
	}

	protected static GLBuffer createBuffer(Object dataArray, GL3 gl, int glBufferType, int glUsageHint, GLBufferMetadata md) {

		checkNotNull(dataArray);
		checkNotNull(md);
		checkArgument(md.isValidPrimitiveType(md.getGlPrimitiveType()));

		GLBuffer glBuffer = new GLBuffer(glBufferType, glUsageHint, md);
		Buffer targetBuffer = null;
		int glSizeIptr = -1;

		switch (md.getGlPrimitiveType()) {
	
			case GL_FLOAT:
				float[] floatData = (float[]) dataArray;
				targetBuffer = newDirectFloatBuffer(floatData);
				glSizeIptr = floatData.length;
				break;
	
			case GL_DOUBLE:
				double[] doubleData = (double[]) dataArray;
				targetBuffer = newDirectDoubleBuffer(doubleData);
				glSizeIptr = doubleData.length;
				break;
	
			case GL_INT:
			case GL_UNSIGNED_INT:
				int[] intData = (int[]) dataArray;
				targetBuffer = newDirectIntBuffer(intData);
				glSizeIptr = intData.length;
				break;
	
			case GL_BYTE:
			case GL_UNSIGNED_BYTE:
				byte[] byteData = (byte[]) dataArray;
				targetBuffer = newDirectByteBuffer(byteData);
				glSizeIptr = byteData.length;
				break;
	
			case GL_SHORT:
			case GL_UNSIGNED_SHORT:
				short[] shortData = (short[]) dataArray;
				targetBuffer = newDirectShortBuffer(shortData);
				glSizeIptr = shortData.length;
				break;
		}

		glSizeIptr = glSizeIptr * md.getByteSizeof(md.getGlPrimitiveType());

		targetBuffer.flip();
		glBuffer.init(gl);
		glBuffer.bind();
		glBuffer.getGl().glBufferData(glBuffer.getGlBufferType(), glSizeIptr, targetBuffer, glUsageHint);
		glBuffer.checkError();
		glBuffer.unbind();
		glBuffer.setRawBuffer(targetBuffer);

		return glBuffer;
	}
}
