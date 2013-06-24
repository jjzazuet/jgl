package org.jgl.geom.solid.model;

import org.jgl.geom.FaceWinding;
import org.jgl.math.vector.Vector4;

public interface Drawable {
	public float [] getVertices();	
	public FaceWinding getFaceWinding();
	public Vector4 getBoundingSphere();
}