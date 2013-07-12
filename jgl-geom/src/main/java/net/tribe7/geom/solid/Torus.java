package net.tribe7.geom.solid;

import static net.tribe7.common.base.Preconditions.*;
import static java.lang.Math.*;
import static net.tribe7.math.angle.AngleOps.*;
import net.tribe7.geom.FaceWinding;
import net.tribe7.geom.io.GeometryBuffer;
import net.tribe7.geom.solid.model.BitangentMapped;
import net.tribe7.geom.solid.model.IndexDrawable;
import net.tribe7.geom.solid.model.IndexRestartable;
import net.tribe7.geom.solid.model.NormalMapped;
import net.tribe7.geom.solid.model.Drawable;
import net.tribe7.geom.solid.model.SphereBound;
import net.tribe7.geom.solid.model.TangentMapped;
import net.tribe7.geom.solid.model.Textured;
import net.tribe7.math.vector.Vector4;

public class Torus implements Drawable, IndexDrawable, Textured, 
	NormalMapped, TangentMapped, BitangentMapped, IndexRestartable, SphereBound {

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
	public GeometryBuffer<Float> getVertices() {

		Float [] vertices = new Float[(rings + 1) * (sections + 1) * 3];
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
		return new GeometryBuffer<Float>(3, vertices);
	}

	@Override
	public int getPrimitiveRestartIndex() {
		return rings * (2 * (sections + 1) + 1);
	}

	@Override
	public GeometryBuffer<Integer> getIndices() {

		int n = getPrimitiveRestartIndex();
		Integer [] indices = new Integer[n];
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
		return new GeometryBuffer<Integer>(3, indices);
	}

	@Override
	public GeometryBuffer<Float> getNormals() {

		Float [] normals = new Float[(rings + 1) * (sections + 1) * 3];
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
		return new GeometryBuffer<Float>(3, normals);
	}

	@Override
	public GeometryBuffer<Float> getTexCoords() {

		Float[] texCoords = new Float [(rings + 1) * (sections + 1) * 2];
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
		return new GeometryBuffer<Float>(2, texCoords);
	}

	@Override
	public GeometryBuffer<Float> getTangents() {

		Float [] tangents = new Float[(rings + 1) * (sections + 1) * 3];
		int k = 0;
		double r_step = TWO_PI / ((double) rings);

		for(int r=0; r!=(rings+1); ++r) {
			
			double tx = -sin(r*r_step);
			double tz = -cos(r*r_step);
			
			for(int s=0; s!=(sections+1); ++s) {
				tangents[k++] = (float) tx;
				tangents[k++] = (float) 0;
				tangents[k++] = (float) tz;
			}
		}
		checkState(k == tangents.length);
		return new GeometryBuffer<Float>(3, tangents);
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
	public GeometryBuffer<Float> getBitangents() {

		Float[] bitangents = new Float [(rings + 1) * (sections + 1) * 3];
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
		return new GeometryBuffer<Float>(3, bitangents);
	}

	public double getRadiusOut() { return radiusOut; }
	public double getRadiusIn() { return radiusIn; }
	public int getRings() { return rings; }
	public int getSections() { return sections; }
}
