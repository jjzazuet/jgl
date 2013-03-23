package org.jgl.opengl.test;

public class TGeometry {

	public static final float[] triangleVertices = new float[] { 
			0.0f, 0.0f, 0.0f, 
			1.0f, 0.0f, 0.0f, 
			0.0f, 1.0f, 0.0f 
	};
	
	public static final float [] rectangle_verts = new float[] {
			-1.0f, -1.0f,
			-1.0f,  1.0f,
			 1.0f, -1.0f,
			 1.0f,  1.0f
	};

	public static final float [] rectangle_colors = new float [] {
			1.0f, 1.0f, 1.0f,
			1.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 1.0f,
	};
	
	public static final float [] rectangle_coords = new float [] {
			-1.5f, -0.5f,
			-1.5f,  1.0f,
			 0.5f, -0.5f,
			 0.5f,  1.0f
	};
	
	public static final float [] color_map = new float [] {
			0.4f, 0.2f, 1.0f, 0.00f,
			1.0f, 0.2f, 0.2f, 0.30f,
			1.0f, 1.0f, 1.0f, 0.95f,
			1.0f, 1.0f, 1.0f, 0.98f,
			0.1f, 0.1f, 0.1f, 1.00f
	};
}
