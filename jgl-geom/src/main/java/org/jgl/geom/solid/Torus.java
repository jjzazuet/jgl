package org.jgl.geom.solid;

import static com.google.common.base.Preconditions.*;
import static java.lang.Math.*;
import static org.jgl.math.angle.AngleOps.*;
import org.jgl.geom.FaceWinding;
import org.jgl.geom.solid.model.BitangentMapped;
import org.jgl.geom.solid.model.IndexDrawable;
import org.jgl.geom.solid.model.NormalMapped;
import org.jgl.geom.solid.model.Drawable;
import org.jgl.geom.solid.model.TangentMapped;
import org.jgl.geom.solid.model.Textured;
import org.jgl.math.vector.Vector4;

public class Torus implements Drawable, IndexDrawable, Textured, NormalMapped, TangentMapped, BitangentMapped {

	private final double radiusOut, radiusIn;
	private final int sections, rings;
	
	public Torus() {
		radiusOut = 1.0;
		radiusIn = 0.5;
		sections = 36;
		rings = 24;
	}

	public Torus(double radiusOut, double radiusIn, int sections, int rings) {
		this.radiusOut = radiusOut;
		this.radiusIn = radiusIn;
		this.sections = sections;
		this.rings = rings;
	}
	
	@Override
	public float[] getVertices() {

		float [] vertices = new float[(rings + 1) * (sections + 1) * 3];
		int k = 0;
		
		double r_step = TWO_PI / ((double) rings);
		double s_step = TWO_PI / ((double) sections);
		double r1 = radiusIn;
		double r2 = radiusOut - radiusIn;

		for(int r=0; r!=(rings+1); ++r) {
			
			double vx =  cos(r*r_step);
			double vz = -sin(r*r_step);
			
			for(int s=0; s!=(sections+1); ++s) {
				double vr = cos(s*s_step);
				double vy = sin(s*s_step);
				vertices[k++] = (float) (vx*(r1 + r2 * (1.0 + vr)));
				vertices[k++] = (float) (vy*r2);
				vertices[k++] = (float) (vz*(r1 + r2 * (1.0 + vr)));
			}
		}
		checkState(k == vertices.length);
		return vertices;
	}

	@Override
	public int[] getIndices() {

		int n = rings * (2 * (sections + 1) + 1);
		int [] indices = new int[n];
		int k = 0;
		int offs = 0;

		for(int r=0; r!=(rings); ++r) {
			for(int s=0; s!=(sections+1); ++s) {
				indices[k++] = offs + s;
				indices[k++] = offs + s + (sections+1);
			}
			indices[k++] = n;
			offs += sections + 1;
		}
		checkState(k == indices.length);
		return indices;
	}

	@Override
	public float[] getNormals() {

		float [] normals = new float[(rings + 1) * (sections + 1) * 3];
		int k = 0;

		double r_step = (TWO_PI) / ((double) rings);
		double s_step = (TWO_PI) / ((double) sections);

		for(int r=0; r!=(rings+1); ++r) {
			
			double nx =  cos(r*r_step);
			double nz = -sin(r*r_step);
			
			for(int s=0; s!=(sections+1); ++s)
			{
				double nr = cos(s*s_step);
				double ny = sin(s*s_step);
				normals[k++] = (float) (nx*nr);
				normals[k++] = (float) (ny);
				normals[k++] = (float) (nz*nr);
			}
		}
		checkState(k == normals.length);
		return normals;
	}

	@Override
	public float[] getTexCoords() {
		
		float[] texCoords = new float [(rings + 1) * (sections + 1) * 2];
		int k = 0;
		double r_step = 1.0 / ((double) rings);
		double s_step = 1.0 / ((double) sections);

		for(int r=0; r!=(rings+1); ++r) {
			
			double u = r*r_step;
			
			for(int s=0; s!=(sections+1); ++s) {
				double v = s*s_step;
				texCoords[k++] = (float) u;
				texCoords[k++] = (float) v;
			}
		}
		checkState(k == texCoords.length);
		return texCoords;
	}

	@Override
	public float[] getTangents() {

		float [] tangents = new float[(rings + 1) * (sections + 1) * 3];
		int k = 0;
		double r_step = TWO_PI / ((double) rings);

		for(int r=0; r!=(rings+1); ++r) {
			
			double tx = -sin(r*r_step);
			double tz = -cos(r*r_step);
			
			for(int s=0; s!=(sections+1); ++s) {
				tangents[k++] = (float) tx;
				tangents[k++] = 0;
				tangents[k++] = (float) tz;
			}
		}
		checkState(k == tangents.length);
		return tangents;
	}

	@Override
	public FaceWinding getFaceWinding() {
		return FaceWinding.CCW;
	}

	@Override
	public Vector4 getBoundingSphere() {
		Vector4 center_and_radius = new Vector4(0, 0, 0, getRadiusOut());
		return center_and_radius;
	}

	@Override
	public float[] getBitangents() {
		
		float[] bitangents = new float [(rings + 1) * (sections + 1) * 3];
		int k = 0;

		double r_step = (TWO_PI) / ((double) rings);
		double s_step = (TWO_PI) / ((double) sections);
		double ty = 0.0;
		
		for(int r=0; r!=(rings+1); ++r) {
			
			double tx = -sin(r*r_step);
			double tz = -cos(r*r_step);

			for(int s=0; s!=(sections+1); ++s) {
				
				double ny = sin(s*s_step);
				double nr = cos(s*s_step);
				double nx = -tz*nr;
				double nz =  tx*nr;

				bitangents[k++] = (float) (ny*tz-nz*ty);
				bitangents[k++] = (float) (nz*tx-nx*tz);
				bitangents[k++] = (float) (nx*ty-ny*tx);
			}
		}
		checkState(k == bitangents.length);
		return bitangents;
	}
	
	public double getRadiusOut() { return radiusOut; }
	public double getRadiusIn() { return radiusIn; }
	public int getRings() { return rings; }
	public int getSections() { return sections; }
}
