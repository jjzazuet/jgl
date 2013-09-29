package net.tribe7.demos.mchochlik.t031MotionBlur;

import static net.tribe7.math.Preconditions.*;
import static net.tribe7.common.base.Preconditions.*;
import static javax.media.opengl.GL2.*;

import java.nio.ByteBuffer;

import net.tribe7.opengl.GLTextureImage;

public class CheckerRedBlack extends GLTextureImage {

	private final byte [] imageData;

	public CheckerRedBlack(int width, int height, int xrep, int yrep) {

		checkArgument(width > ZERO);
		checkArgument(height > ZERO);
		checkArgument(xrep > ZERO);
		checkArgument(yrep > ZERO);
		getMetadata().setWidth(width);
		getMetadata().setHeight(height);
		getMetadata().setPixelDataFormat(GL_RED);
		getMetadata().setInternalFormat(GL_RED);
		getMetadata().setPixelDataType(GL_BYTE);

		imageData = new byte[width * height];
		ByteBuffer b = ByteBuffer.wrap(imageData);
		int xdiv = width / xrep;
		int ydiv = height/ yrep;

		b.clear();
		for (int j = 0; j != height; ++j) {
			int y = j / ydiv;
			for (int i = 0; i != width; ++i) {
				int x = i / xdiv;
				int value = ((x + y) % 2 == 0) ? 0x00 : 0xFF;
				byte c = (byte) value;
				b.put(c);
			}
		}
		b.flip();
		setImageData(b);
	}
}
