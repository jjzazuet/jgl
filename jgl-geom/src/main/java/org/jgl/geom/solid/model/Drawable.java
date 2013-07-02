package org.jgl.geom.solid.model;

import org.jgl.geom.FaceWinding;
import org.jgl.geom.io.GeometryBuffer;

public interface Drawable {
	public GeometryBuffer<Float> getVertices();
	public FaceWinding getFaceWinding();
}