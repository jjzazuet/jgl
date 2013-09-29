package net.tribe7.geom.plane;

import net.tribe7.geom.FaceWinding;
import net.tribe7.geom.io.GeometryBuffer;
import net.tribe7.geom.solid.model.*;
import net.tribe7.math.vector.Vector4;

public class Screen implements Drawable, Textured, 
	NormalMapped, TangentMapped, BitangentMapped, SphereBound {

	@Override
	public Vector4 getBoundingSphere() {
		return new Vector4(0, 0, 0, 1);
	}

	@Override
	public GeometryBuffer<Float> getBitangents() {
		int k = 0;
		Float [] dest = new Float[12];
		for(int i=0; i!=4; ++i) {
			dest[k++] = 0f;
			dest[k++] = 1f;
			dest[k++] = 0f;
		}
		return new GeometryBuffer<Float>(3, dest);
	}

	@Override
	public GeometryBuffer<Float> getTangents() {
		int k = 0;
		Float [] dest = new Float[12];
		for(int i=0; i!=4; ++i) {
			dest[k++] = 1f;
			dest[k++] = 0f;
			dest[k++] = 0f;
		}
		return new GeometryBuffer<Float>(3, dest);
	}

	@Override
	public GeometryBuffer<Float> getNormals() {
		int k = 0;
		Float [] dest = new Float[12];
		for(int i=0; i!=4; ++i) {
			dest[k++] = 0f;
			dest[k++] = 0f;
			dest[k++] = 1f;
		}
		return new GeometryBuffer<Float>(3, dest);
	}

	@Override
	public GeometryBuffer<Float> getTexCoords() {
		int k = 0;
		Float [] dest = new Float[8];

		dest[k++] = 0f;
		dest[k++] = 0f;
		dest[k++] = 0f;
		dest[k++] = 1f;
		dest[k++] = 1f;
		dest[k++] = 0f;
		dest[k++] = 1f;
		dest[k++] = 1f;

		return new GeometryBuffer<Float>(2, dest);
	}

	@Override
	public GeometryBuffer<Float> getVertices() {
		int k = 0;
		Float [] dest = new Float[8];

		dest[k++] = -1f;
		dest[k++] = -1f;
		dest[k++] = -1f;
		dest[k++] = 1f;
		dest[k++] = 1f;
		dest[k++] = -1f;
		dest[k++] = 1f;
		dest[k++] = 1f;

		return new GeometryBuffer<Float>(2, dest);
	}

	@Override
	public FaceWinding getFaceWinding() {
		return FaceWinding.CW;
	}
}
