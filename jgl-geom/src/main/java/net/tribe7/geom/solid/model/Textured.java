package net.tribe7.geom.solid.model;

import net.tribe7.geom.io.GeometryBuffer;

public interface Textured {
	public GeometryBuffer<Float> getTexCoords();
}
