package org.jgl.geom.solid.model;

import org.jgl.geom.FaceWinding;

public interface Drawable {
	public float [] getVertices();	
	public FaceWinding getFaceWinding();
}