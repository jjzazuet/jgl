package net.tribe7.math.matrix.test;

import static net.tribe7.math.matrix.MatrixOps.*;
import net.tribe7.math.matrix.Matrix;

import org.junit.Test;

public class MatrixTests {

	@Test
	public void matrixTests() {

		Matrix m = new Matrix(5, 2);
		Matrix m1 = new Matrix(5, 2);
		Matrix m2 = new Matrix(4, 2);

		m.m(0, 0, 1); m.m(1, 0, 3); m.m(2, 0, 5); m.m(3, 0, 7); m.m(4, 0, 9);
		m.m(0, 1, 2); m.m(1, 1, 4); m.m(2, 1, 6); m.m(3, 1, 8); m.m(4, 1, 10);

		copy(m, m1);
		System.out.println(m);

		m2.m(0, 0, 1); m2.m(1, 0, 2); m2.m(2, 0, 3); m2.m(3, 0, 4);
		m2.m(0, 1, 5); m2.m(1, 1, 6); m2.m(2, 1, 7); m2.m(3, 1, 8);

		Matrix m2t = transpose(m2);
		System.out.println(m2t);
	}
}
