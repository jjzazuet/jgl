package net.tribe7.math.matrix;

import static java.lang.String.format;
import static net.tribe7.math.Preconditions.*;
import static net.tribe7.common.base.Preconditions.*;

@ColumnMajorOrder
public class Matrix {

	private final int componentSize,  columnCount, rowCount;
	private final double [][] data;
	private final StringBuilder mb = new StringBuilder();
	private final StringBuilder sb = new StringBuilder();

	public Matrix(int columns, int rows) {
		checkArgument(columns > ZERO);
		checkArgument(rows > ZERO);
		this.data = new double[columns][rows];
		this.componentSize = columns * rows;
		this.columnCount = columns;
		this.rowCount = rows;
	}

	public Matrix(int dimension) { this(dimension, dimension); }

	public void m(int i, int j, double v) {
		checkIndexes(i, j);
		this.data[i][j] = v;
	}

	public double m(int i, int j) {
		checkIndexes(i, j);
		return data[i][j];
	}

	private void checkIndexes(int i, int j) {
		checkElementIndex(i, data.length);
		checkElementIndex(j, data[i].length);
	}

	public Matrix checkDimensions(Matrix o) {
		checkNotNull(o);
		checkArgument(getColumnCount() == o.getColumnCount());
		checkArgument(getRowCount() == o.getRowCount());
		return o;
	}

	public boolean isSquare() {
		return getColumnCount() == getRowCount();
	}

	public int getComponentSize() { return componentSize; }
	public int getColumnCount() { return columnCount; }
	public int getRowCount() { return rowCount; }

	@Override
	public String toString() {
		mb.append(format("%s%n", getClass().getSimpleName()));
		for (int j = ZERO; j < getRowCount(); j++) {
			sb.setLength(ZERO);
			sb.append("[ ");
			for (int i = 0; i < getColumnCount(); i++) {
				sb.append(format("%.6f ", data[i][j]));
			}
			sb.append("]\n");
			mb.append(sb.toString());
		}
		String ms = mb.toString();
		mb.setLength(ZERO);
		return ms;
	}
}
