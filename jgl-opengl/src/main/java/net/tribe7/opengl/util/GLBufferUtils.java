package net.tribe7.opengl.util;

import static net.tribe7.common.base.Preconditions.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class GLBufferUtils {

	public static byte[] primitiveArray(Byte [] i) {
		checkNotNull(i);
		byte [] arr = new byte[i.length];
		for (int k = 0; k < i.length; k++) {
			arr[k] = i[k];
		}
		return arr;
	}

	public static short[] primitiveArray(Short [] i) {
		checkNotNull(i);
		short [] arr = new short[i.length];
		for (int k = 0; k < i.length; k++) {
			arr[k] = i[k];
		}
		return arr;
	}

	public static double[] primitiveArray(Double [] i) {
		checkNotNull(i);
		double [] arr = new double[i.length];
		for (int k = 0; k < i.length; k++) {
			arr[k] = i[k];
		}
		return arr;
	}

	public static float[] primitiveArray(Float [] i) {
		checkNotNull(i);
		float [] arr = new float[i.length];
		for (int k = 0; k < i.length; k++) {
			arr[k] = i[k];
		}
		return arr;
	}

	public static int[] primitiveArray(Integer [] i) {
		checkNotNull(i);
		int [] arr = new int[i.length];
		for (int k = 0; k < i.length; k++) {
			arr[k] = i[k];
		}
		return arr;
	}

	public static int[] toIntArray(List<Integer> list) {
		checkNotNull(list);
		checkArgument(list.size() > 0);
		int[] ret = new int[list.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = list.get(i);
		return ret;
	}
	
	public static final IntBuffer intBuffer(int ... i) {
		checkNotNull(i);
		checkArgument(i.length > 0);
		IntBuffer b = IntBuffer.wrap(i);
		b.flip();
		return b;
	}
	
	public static final FloatBuffer floatBuffer(float ... f) {
		checkNotNull(f);
		checkArgument(f.length > 0);
		FloatBuffer fb = FloatBuffer.wrap(f);
		fb.flip();
		return fb;
	}
}
