package org.jgl.geom.solid.model;

import org.jgl.geom.io.GeometryBuffer;

public interface Textured {
	public GeometryBuffer<Float> getTexCoords();
}
