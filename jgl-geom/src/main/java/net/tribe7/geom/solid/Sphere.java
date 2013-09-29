package net.tribe7.geom.solid;

import static net.tribe7.common.base.Preconditions.*;
import static java.lang.Math.*;

import net.tribe7.geom.FaceWinding;
import net.tribe7.geom.io.GeometryBuffer;
import net.tribe7.geom.solid.model.IndexDrawable;
import net.tribe7.geom.solid.model.IndexRestartable;
import net.tribe7.geom.solid.model.NormalMapped;
import net.tribe7.geom.solid.model.Drawable;
import net.tribe7.geom.solid.model.SphereBound;
import net.tribe7.geom.solid.model.TangentMapped;
import net.tribe7.geom.solid.model.Textured;
import net.tribe7.math.vector.Vector4;

public class Sphere implements Drawable, IndexDrawable, Textured, 
	NormalMapped, TangentMapped, SphereBound, IndexRestartable {

	private double radius;
	private int sections, rings;
	
	public Sphere(double radius, int sections, int rings) {
		this.radius = radius;
		this.sections = sections;
		this.rings = rings;
	}

	public Sphere() { this(1, 18, 12); }

	@Override
	public GeometryBuffer<Float> getVertices() {

		Float [] n = getNormals().getData();

		if (getRadius() != 1) {
			for (int i = 0; i < n.length; i++) {
				n[i] = (float) (n[i] * getRadius());
			}
		}
		return new GeometryBuffer<Float>(3, n);
	}

	@Override
	public GeometryBuffer<Integer> getIndices() {

		int n = (getRings() + 1)*(2 * (getSections() + 1) + 1);
		Integer [] indices = new Integer [n];
		int k = 0;
		int offs = 0;

		for(int r = 0; r!= (getRings() + 1); ++r) {
			for(int s = 0; s != (getSections() + 1); ++s) {
				indices[k++] = offs + s;
				indices[k++] = offs + s + (getSections() + 1);
			}
			indices[k++] = n;
			offs += getSections() + 1;
		}

		checkState(k == indices.length);
		return new GeometryBuffer<Integer>(3, indices);
	}

	@Override
	public GeometryBuffer<Float> getNormals() {

		Float [] normals = new Float [((getRings() + 2) * (getSections() + 1)) * 3];
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
		return new GeometryBuffer<Float>(3, normals);
	}

	@Override
	public GeometryBuffer<Float> getTexCoords() {

		Float [] texCoords = new Float [((getRings() + 2) * (getSections() + 1)) * 2];
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

		checkState(k == texCoords.length);
		return new GeometryBuffer<Float>(2, texCoords);
	}

	@Override
	public GeometryBuffer<Float> getTangents() {

		Float [] dest = new Float[((getRings() + 2) * (getSections() + 1)) * 3];
		int k = 0;
		double s_step = (2.0 * PI) / ((double) getSections());

		for(int r = 0; r != (getRings() + 2); ++r) {
			for(int s = 0; s != (getSections() + 1); ++s) {
				dest[k++] = (float) -sin(s*s_step);
				dest[k++] = (float) 0;
				dest[k++] = (float) -cos(s*s_step);
			}
		}

		checkState(k == dest.length);
		return new GeometryBuffer<Float>(3, dest);
	}

	@Override
	public FaceWinding getFaceWinding() {
		return FaceWinding.CCW;
	}

	@Override
	public Vector4 getBoundingSphere() {
		return new Vector4(0, 0, 0, getRadius());
	}

	@Override
	public int getPrimitiveRestartIndex() {
		return (getRings() + 1)*(2 * (getSections() + 1) + 1);
	}

	public double getRadius() { return radius; }
	public int getRings() { return rings; }
	public int getSections() { return sections; }
}
