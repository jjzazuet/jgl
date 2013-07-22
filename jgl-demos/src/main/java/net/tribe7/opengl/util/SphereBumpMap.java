package net.tribe7.opengl.util;

import static net.tribe7.math.vector.VectorOps.*;
import static javax.media.opengl.GL.*;
import static net.tribe7.common.base.Preconditions.*;

import java.nio.*;
import java.util.*;

import net.tribe7.math.vector.Vector3;
import net.tribe7.opengl.GLTextureImage;
import net.tribe7.common.primitives.Floats;

public class SphereBumpMap extends GLTextureImage {

	public static final int ZERO = 0;
	private int xrep = 1;
	private int yrep = 1;
	private FloatBuffer imageDataArray = null;
	
	public SphereBumpMap(int width, int height, int xrep, int yrep) {

		checkArgument(width > ZERO);
		checkArgument(height > ZERO);
		checkArgument(xrep > ZERO);
		checkArgument(yrep > ZERO);

		getMetadata().setWidth(width);
		getMetadata().setHeight(height);
		this.xrep = xrep;
		this.yrep = yrep;

		getMetadata().setPixelDataFormat(GL_RGBA);
		getMetadata().setInternalFormat(GL_RGBA16F);
		getMetadata().setPixelDataType(GL_FLOAT);
	}

	@Override
	public Buffer getImageData() {

		if (imageDataArray == null) {

			int width = getMetadata().getWidth();
			int height = getMetadata().getHeight();
			double one = 1;
			double invw = (2.0f*xrep)/width;
			double invh = (2.0f*yrep)/height;
			int hi = width / xrep;
			int hj = height/yrep;

			List<Float> data = new ArrayList<Float>();
			Vector3 z = new Vector3();
			Vector3 n = new Vector3();
			Vector3 v = new Vector3();

			for(int j=0; j!=height; ++j) {

				double y = ((j % hj) - hj/2) * invh;

				for(int i=0; i!=width; ++i) {

					double x = ((i % hi) - hi/2)*invw;
					double l = Math.sqrt(x*x + y*y);
					double d = Math.sqrt(one-l*l);

					z.set(0.0, 0.0, one);
					n.set(-x, -y, d);
					
					if (l >= one) {
						v.set(z);
					} else {
						add(z, n, v);
						normalize(n);
					}

					if(l >= one) d = 0;

					data.add((float) v.x);
					data.add((float) v.y);
					data.add((float) v.z);
					data.add((float) d);
				}
			}

			float [] dataArray = Floats.toArray(data);
			imageDataArray = FloatBuffer.wrap(dataArray);
			setImageData(imageDataArray);
		}

		return imageDataArray;
	}
}
