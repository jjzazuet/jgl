package org.jgl.geom.solid;

import static org.jgl.geom.FaceWinding.*;

import org.jgl.geom.FaceWinding;
import org.jgl.geom.solid.model.IndexDrawable;
import org.jgl.geom.solid.model.NormalMapped;
import org.jgl.geom.solid.model.Drawable;
import org.jgl.geom.solid.model.SphereBound;
import org.jgl.geom.solid.model.TangentMapped;
import org.jgl.geom.solid.model.Textured;
import org.jgl.math.vector.Vector4;
import static com.google.common.base.Preconditions.*;

/**
 * <pre>
 *   (E)-----(A)
 *   /|      /|
 *  / |     / |
 *(F)-----(B) |
 * | (H)---|-(D)
 * | /     | /
 * |/      |/
 *(G)-----(C)
 * </pre>
 */
public class Cube implements Drawable, IndexDrawable, Textured, NormalMapped, TangentMapped, SphereBound {

	private double x, y, z;
	
	public Cube() { this(1, 1, 1); }
	public Cube(double size) { this(size, size, size); }
	
	public Cube(double w, double h, double d) {
		
		checkArgument(w > 0);
		checkArgument(h > 0);
		checkArgument(d > 0);
		
		x = w; y = h; z = d;
	}

	@Override
	public float[] getVertices() {
		
		float hX = (float) (x / 2);
		float hY = (float) (y / 2);
		float hZ = (float) (z / 2);
		
		float [][] c = new float [][] {
				{+hX, +hY, -hZ},
				{+hX, +hY, +hZ},
				{+hX, -hY, +hZ},
				{+hX, -hY, -hZ},
				{-hX, +hY, -hZ},
				{-hX, +hY, +hZ},
				{-hX, -hY, +hZ},
				{-hX, -hY, -hZ}
		};
		
		int A=0, B=1, C=2, D=3, E=4, F=5, G=6, H=7;
		
		float [] positions = new float [] {
				
				c[A][0], c[A][1], c[A][2],
				c[D][0], c[D][1], c[D][2],
				c[B][0], c[B][1], c[B][2],
				c[B][0], c[B][1], c[B][2],
				c[D][0], c[D][1], c[D][2],
				c[C][0], c[C][1], c[C][2],
		
				c[A][0], c[A][1], c[A][2],
				c[B][0], c[B][1], c[B][2],
				c[E][0], c[E][1], c[E][2],
				c[B][0], c[B][1], c[B][2],
				c[F][0], c[F][1], c[F][2],
				c[E][0], c[E][1], c[E][2],
		
				c[B][0], c[B][1], c[B][2],
				c[C][0], c[C][1], c[C][2],
				c[F][0], c[F][1], c[F][2],
				c[C][0], c[C][1], c[C][2],
				c[G][0], c[G][1], c[G][2],
				c[F][0], c[F][1], c[F][2],
		
				c[E][0], c[E][1], c[E][2],
				c[F][0], c[F][1], c[F][2],
				c[G][0], c[G][1], c[G][2],
				c[E][0], c[E][1], c[E][2],
				c[G][0], c[G][1], c[G][2],
				c[H][0], c[H][1], c[H][2],
		
				c[C][0], c[C][1], c[C][2],
				c[D][0], c[D][1], c[D][2],
				c[H][0], c[H][1], c[H][2],
				c[C][0], c[C][1], c[C][2],
				c[H][0], c[H][1], c[H][2],
				c[G][0], c[G][1], c[G][2],
		
				c[A][0], c[A][1], c[A][2],
				c[E][0], c[E][1], c[E][2],
				c[D][0], c[D][1], c[D][2],
				c[D][0], c[D][1], c[D][2],
				c[E][0], c[E][1], c[E][2],
				c[H][0], c[H][1], c[H][2]
		};
		
		return positions;
	}

	@Override
	public int[] getIndices() {
		
		int [] a = new int[] { 
				0,   1,  5,  2, // +x
				19, 22, 23, 18, // -x
				6,   7, 10, 11, // +y
				26, 29, 24, 25, // -y
				12, 13, 16, 17, // +z
				31, 35, 32, 30  // -z
		};
		
		return a;
	}

	@Override
	public FaceWinding getFaceWinding() { return CW; }

	@Override
	public float[] getNormals() {

		float [][] n = new float [][] {
				{ 1, 0, 0 },
				{ 0, 1, 0 },
				{ 0, 0, 1 },
				{-1, 0, 0 },
				{ 0,-1, 0 },
				{ 0, 0,-1 }
			};
		
		float [] normals = new float [] {
				
				n[0][0], n[0][1], n[0][2],
				n[0][0], n[0][1], n[0][2],
				n[0][0], n[0][1], n[0][2],
				n[0][0], n[0][1], n[0][2],
				n[0][0], n[0][1], n[0][2],
				n[0][0], n[0][1], n[0][2],

				n[1][0], n[1][1], n[1][2],
				n[1][0], n[1][1], n[1][2],
				n[1][0], n[1][1], n[1][2],
				n[1][0], n[1][1], n[1][2],
				n[1][0], n[1][1], n[1][2],
				n[1][0], n[1][1], n[1][2],

				n[2][0], n[2][1], n[2][2],
				n[2][0], n[2][1], n[2][2],
				n[2][0], n[2][1], n[2][2],
				n[2][0], n[2][1], n[2][2],
				n[2][0], n[2][1], n[2][2],
				n[2][0], n[2][1], n[2][2],

				n[3][0], n[3][1], n[3][2],
				n[3][0], n[3][1], n[3][2],
				n[3][0], n[3][1], n[3][2],
				n[3][0], n[3][1], n[3][2],
				n[3][0], n[3][1], n[3][2],
				n[3][0], n[3][1], n[3][2],

				n[4][0], n[4][1], n[4][2],
				n[4][0], n[4][1], n[4][2],
				n[4][0], n[4][1], n[4][2],
				n[4][0], n[4][1], n[4][2],
				n[4][0], n[4][1], n[4][2],
				n[4][0], n[4][1], n[4][2],

				n[5][0], n[5][1], n[5][2],
				n[5][0], n[5][1], n[5][2],
				n[5][0], n[5][1], n[5][2],
				n[5][0], n[5][1], n[5][2],
				n[5][0], n[5][1], n[5][2],
				n[5][0], n[5][1], n[5][2]
		};
		
		return normals;
	}

	@Override
	public float[] getTexCoords() {

		float [] a = new float [] {
				
				1, 1, 0,
				1, 0, 0,
				0, 1, 0,
				0, 1, 0,
				1, 0, 0,
				0, 0, 0,

				1, 1, 1,
				1, 0, 1,
				0, 1, 1,
				1, 0, 1,
				0, 0, 1,
				0, 1, 1,

				1, 1, 2,
				1, 0, 2,
				0, 1, 2,
				1, 0, 2,
				0, 0, 2,
				0, 1, 2,

				0, 1, 3,
				1, 1, 3,
				1, 0, 3,
				0, 1, 3,
				1, 0, 3,
				0, 0, 3,

				1, 1, 4,
				1, 0, 4,
				0, 0, 4,
				1, 1, 4,
				0, 0, 4,
				0, 1, 4,

				0, 1, 5,
				1, 1, 5,
				0, 0, 5,
				0, 0, 5,
				1, 1, 5,
				1, 0, 5
			};
		
		return a;
	}

	@Override
	public float[] getTangents() {
		
		float [][] n = new float [][] {
				{ 0,  0, -1},
				{+1,  0,  0},
				{+1,  0,  0},
				{ 0,  0, +1},
				{-1,  0,  0},
				{-1,  0,  0}
			};
		
		
		float []_tangents = new float [] {
				n[0][0], n[0][1], n[0][2],
				n[0][0], n[0][1], n[0][2],
				n[0][0], n[0][1], n[0][2],
				n[0][0], n[0][1], n[0][2],
				n[0][0], n[0][1], n[0][2],
				n[0][0], n[0][1], n[0][2],

				n[1][0], n[1][1], n[1][2],
				n[1][0], n[1][1], n[1][2],
				n[1][0], n[1][1], n[1][2],
				n[1][0], n[1][1], n[1][2],
				n[1][0], n[1][1], n[1][2],
				n[1][0], n[1][1], n[1][2],

				n[2][0], n[2][1], n[2][2],
				n[2][0], n[2][1], n[2][2],
				n[2][0], n[2][1], n[2][2],
				n[2][0], n[2][1], n[2][2],
				n[2][0], n[2][1], n[2][2],
				n[2][0], n[2][1], n[2][2],

				n[3][0], n[3][1], n[3][2],
				n[3][0], n[3][1], n[3][2],
				n[3][0], n[3][1], n[3][2],
				n[3][0], n[3][1], n[3][2],
				n[3][0], n[3][1], n[3][2],
				n[3][0], n[3][1], n[3][2],

				n[4][0], n[4][1], n[4][2],
				n[4][0], n[4][1], n[4][2],
				n[4][0], n[4][1], n[4][2],
				n[4][0], n[4][1], n[4][2],
				n[4][0], n[4][1], n[4][2],
				n[4][0], n[4][1], n[4][2],

				n[5][0], n[5][1], n[5][2],
				n[5][0], n[5][1], n[5][2],
				n[5][0], n[5][1], n[5][2],
				n[5][0], n[5][1], n[5][2],
				n[5][0], n[5][1], n[5][2],
				n[5][0], n[5][1], n[5][2]
			};

		return _tangents;
	}

	@Override
	public Vector4 getBoundingSphere() {
		return new Vector4(0, 0, 0, x*x + y*y + z+z);
	}
}
