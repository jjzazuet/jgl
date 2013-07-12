package net.tribe7.geom.bezier;

import static java.lang.String.format;
import static net.tribe7.geom.bezier.BezierOps.*;
import static net.tribe7.common.base.Preconditions.*;
import net.tribe7.math.vector.Vector3;

public class BezierCurve4 {

	public static final int DEGREE = 4;
	private Vector3 p0, p1, p2, p3, c0;
	
	public BezierCurve4(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3) {
		this.p0 = checkNotNull(p0);
		this.p1 = checkNotNull(p1);
		this.p2 = checkNotNull(p2);
		this.p3 = checkNotNull(p3);
		this.c0 = new Vector3();
	}

	public Vector3 pointAt(double t) {
		bezierPointCubic(t, p0, p1, p2, p3, c0);
		return c0;
	}
	
	@Override
	public String toString() {
		return format("%s[%np0:%s, h0:%s, h1:%s, p1:%s]%n", 
				getClass().getSimpleName(), 
				p0, p1, p2, p3);
	}
}
