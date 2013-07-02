package org.jgl.geom.solid.model;

import org.jgl.geom.io.GeometryBuffer;

public interface TangentMapped {
	public GeometryBuffer<Float> getTangents();
}
