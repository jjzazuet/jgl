package org.jgl.geom;

import static com.google.common.base.Preconditions.*;
import static java.lang.Math.*;
import org.jgl.math.vector.Vector4;

public class Sphere implements Shape {

	private double radius;
	private int sections, rings;
	
	public Sphere(double radius, int sections, int rings) {
		this.radius = radius;
		this.sections = sections;
		this.rings = rings;
	}
	
	public Sphere() { this(1, 18, 12); }

	@Override
	public float[] getVertices() {
		
		float [] n = getNormals();
		
		if (getRadius() != 1) {
			for (int i = 0; i < n.length; i++) {
				n[i] = (float) (n[i] * getRadius());
			}
		}
		
		return n;
	}

	@Override
	public int[] getIndices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float[] getNormals() {

		float [] normals = new float [((getRings() + 2) * (getSections() + 1)) * 3];
		int k = 0;
		double rStep = (1 * PI) / ((double) (getRings() + 1));
		double sStep = (2 * PI) / ((double) getSections());
		
		for (int r = 0; r != (getRings() + 2); ++r) {

			double r_lat = cos(r * rStep);
			double r_rad = sin(r * rStep);

			for(int s = 0; s != ((double) getSections() + 1); ++s) {
				normals[k++] = (float) (r_rad * cos(s * sStep));
				normals[k++] = (float) r_lat;
				normals[k++] = (float) (r_rad * -sin(s * sStep));
			}
		}
		
		checkState(k == normals.length);
		return normals; // TODO report resulting component size.
	}

	@Override
	public float[] getTexCoords() {
		
		float [] texCoords = new float [((getRings() + 2) * (getSections() + 1)) * 2];
		int k = 0;
		double r_step = 1.0 / ((double) (getRings() + 1));
		double s_step = 1.0 / ((double) getSections());
		
		for(int r=0; r!= (getRings() + 2); ++r) {
			
			double r_lat = 1.0 - r*r_step;
			
			for(int s=0; s != getSections() + 1; ++s) {
				texCoords[k++] = (float) (s * s_step);
				texCoords[k++] = (float) r_lat;
			}
		}
		
		checkState(k == texCoords.length); // 2 values per vertex
		return texCoords;
	}

	@Override
	public float[] getTangents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FaceOrientation getFaceWinding() {
		return FaceOrientation.CCW;
	}

	@Override
	public Vector4 getBoundingSphere() {
		// TODO Auto-generated method stub
		return new Vector4(0, 0, 0, getRadius());
	}

	public double getRadius() { return radius; }
	public int getRings() { return rings; }
	public int getSections() { return sections; }
}
