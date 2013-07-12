package net.tribe7.geom.solid.model;

import net.tribe7.geom.FaceWinding;
import net.tribe7.geom.io.GeometryBuffer;

public interface Drawable {
	public GeometryBuffer<Float> getVertices();
	public FaceWinding getFaceWinding();
}