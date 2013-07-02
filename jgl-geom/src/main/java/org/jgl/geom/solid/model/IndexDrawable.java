package org.jgl.geom.solid.model;

import org.jgl.geom.io.GeometryBuffer;

public interface IndexDrawable {
	public GeometryBuffer<Integer> getIndices();
}
