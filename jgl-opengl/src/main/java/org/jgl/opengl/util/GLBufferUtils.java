package org.jgl.opengl.util;

import static com.google.common.base.Preconditions.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class GLBufferUtils {

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
