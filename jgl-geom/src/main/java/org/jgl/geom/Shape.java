package org.jgl.geom;

import org.jgl.math.vector.Vector4;

public interface Shape {
	
	public float [] getVertices();
	public int   [] getIndices();
	public float [] getNormals();
	public float [] getTexCoords();
	public float [] getTangents();
	
	public FaceOrientation getFaceWinding();
	public Vector4 getBoundingSphere();
}