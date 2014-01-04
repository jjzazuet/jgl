package net.tribe7.opengl.camera;

import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.math.Preconditions.*;
import net.tribe7.math.angle.Angle;
import net.tribe7.math.matrix.io.BufferedMatrix4;
import net.tribe7.math.vector.Vector3;

public class GLPointCamera {

	private final Vector3 position = new Vector3();
	private final Vector3 target = new Vector3();

	private final Angle fov = new Angle();
	private final Angle azimuth = new Angle();
	private final Angle elevation = new Angle();

	private final BufferedMatrix4 matrix = new BufferedMatrix4();
	private final BufferedMatrix4 projection = new BufferedMatrix4();

	public GLPointCamera() { this(ONE); }

	public GLPointCamera(double fovDegrees) {
		checkArgument(fovDegrees > ZERO);
		getFov().setDegrees(fovDegrees);
	}

	public Vector3 getPosition() { return position; }
	public Vector3 getTarget() { return target; }

	public Angle getFov() { return fov; }
	public Angle getAzimuth() { return azimuth; }
	public Angle getElevation() { return elevation; }

	public BufferedMatrix4 getMatrix() { return matrix; }
	public BufferedMatrix4 getProjection() { return projection; }
}
