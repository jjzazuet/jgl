package net.tribe7.demos.mchochlik.t031MotionBlur;

import static net.tribe7.math.matrix.MatrixOps.*;
import static net.tribe7.math.vector.VectorOps.*;
import static net.tribe7.math.vector.Vector3Ops.*;

import java.util.ArrayList;
import java.util.List;

import net.tribe7.geom.bezier.BezierCubicLoop;
import net.tribe7.math.matrix.Matrix;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.Vector3;

public class MatrixInstances {

	private final int count;
	private final BezierCubicLoop pathPositions, pathNormals;
	private final List<BufferedMatrix4> matrixData = new ArrayList<BufferedMatrix4>();

	private static final Vector3 [] positionData = new Vector3 [] {
		new Vector3( 10.0f,  0.0f, 70.0f),
		new Vector3( 60.0f,  0.0f, 60.0f),
		new Vector3( 95.0f,  0.0f,  0.0f),
		new Vector3( 60.0f,  0.0f,-60.0f),
		new Vector3( 10.0f,  0.0f,-70.0f),
		new Vector3(-30.0f, 20.0f,-70.0f),
		new Vector3(-70.0f, 40.0f,-70.0f),
		new Vector3(-90.0f, 40.0f,-30.0f),
		new Vector3(-30.0f, 40.0f, 10.0f),
		new Vector3( 40.0f, 40.0f, 10.0f),
		new Vector3( 90.0f,  0.0f,  0.0f),
		new Vector3( 50.0f,-40.0f,-10.0f),
		new Vector3(  0.0f,-40.0f,-10.0f),
		new Vector3(-50.0f,-40.0f,-10.0f),
		new Vector3(-90.0f,-40.0f, 30.0f),
		new Vector3(-70.0f,-40.0f, 70.0f),
		new Vector3(-30.0f,-20.0f, 70.0f)
	};

	private static final Vector3 [] normalData = new Vector3 [] {
		new Vector3( 0.0f,  1.0f,  0.0f),
		new Vector3( 0.0f,  1.0f, -1.0f),
		new Vector3(-1.0f,  0.0f,  0.0f),
		new Vector3( 0.0f,  1.0f,  2.0f),
		new Vector3( 0.0f,  1.0f,  1.0f),
		new Vector3( 1.0f,  1.0f,  0.5f),
		new Vector3( 0.0f,  1.0f,  1.0f),
		new Vector3( 1.0f,  0.0f,  0.0f),
		new Vector3( 0.0f,  1.0f, -1.0f),
		new Vector3( 0.0f,  1.0f,  0.0f),
		new Vector3( 1.0f,  0.0f,  0.0f),
		new Vector3( 0.0f, -1.0f,  1.0f),
		new Vector3( 0.0f,  0.0f,  1.0f),
		new Vector3( 0.0f,  1.0f,  1.0f),
		new Vector3( 1.0f,  0.0f,  0.0f),
		new Vector3( 0.0f,  1.0f, -1.0f),
		new Vector3(-1.0f,  1.0f,  0.0f)
	};

	public MatrixInstances(int n) {

		count = 2 * n;
		pathPositions = new BezierCubicLoop(positionData);

		for (Vector3 v : normalData) { normalize(v);}

		pathNormals = new BezierCubicLoop(normalData);
		double step = 1.0/n;

		for(int i=0; i!=n; ++i) {

			Vector3 pos = pathPositions.pointAt(i*step);
			Vector3 tgt = new Vector3();

			sub(pathPositions.pointAt((i+1)*step), pathPositions.pointAt((i-1)*step), tgt);
			normalize(tgt);

			Vector3 tmp = pathNormals.pointAt(i*step);
			Vector3 nml = new Vector3();

			if (dot(tmp, tgt) != 0.0) {
				scale(tgt, dot(tmp, tgt));
				sub(tmp, tgt, nml);
			} else {
				nml.set(tmp);
			}

			normalize(nml);

			Vector3 btg = new Vector3();
			cross(nml, tgt, btg);

			for(int j=0; j!=2; ++j) {

				float s[] = {-3.0f, 3.0f};
				BufferedMatrix4 m = new BufferedMatrix4();
				Vector3 r4 = new Vector3();

				scale(btg, r4, s[j]);
				add(pos, r4, r4);

				m.m(0, 0, btg.x); m.m(1, 0, btg.y); m.m(2, 0, btg.z); m.m(3, 0, 0); 
				m.m(0, 1, nml.x); m.m(1, 1, nml.y); m.m(2, 1, nml.z); m.m(3, 1, 0); 
				m.m(0, 2, tgt.x); m.m(1, 2, tgt.y); m.m(2, 2, tgt.z); m.m(3, 2, 0); 
				m.m(0, 3,  r4.x); m.m(1, 3,  r4.y); m.m(2, 3,  r4.z); m.m(3, 3, 1); 

				matrixData.add(m);
			}
		}
	}

	Vector3 position(double t) {
		return pathPositions.pointAt(t);
	}

	Vector3 normal(double t) {
		return pathNormals.pointAt(t);
	}

	BufferedMatrix4 makeMatrix(double t, double dt) {

		Vector3 pos = position(t);
		Vector3 tgt = new Vector3();

		sub(position(t+dt), position(t-dt), tgt);
		normalize(tgt);

		Vector3 tmp = normal(t+dt);
		float dtt = (float) dot(tmp, tgt);

		Vector3 nml = new Vector3();

		if (dtt != 0.0) {
			scale(tgt, nml, dtt);
			sub(tmp, nml, nml);
		} else {
			nml.set(tmp);
		}

		normalize(nml);
		Vector3 btg = new Vector3();

		cross(nml, tgt, btg);

		Matrix r = new BufferedMatrix4();

		r.m(0, 0, btg.x); r.m(1, 0, btg.y); r.m(2, 0, btg.z); r.m(3, 0, 0);
		r.m(0, 1, nml.x); r.m(1, 1, nml.y); r.m(2, 1, nml.z); r.m(3, 1, 0);
		r.m(0, 2, tgt.x); r.m(1, 2, tgt.y); r.m(2, 2, tgt.z); r.m(3, 2, 0);
		r.m(0, 3, pos.x); r.m(1, 3, pos.y); r.m(2, 3, pos.z); r.m(3, 3, 1);

		Matrix rt = transpose(r);
		BufferedMatrix4 result = new BufferedMatrix4();

		copy(rt, result);
		return result;
	}

	public int getCount() { return count; }
}
