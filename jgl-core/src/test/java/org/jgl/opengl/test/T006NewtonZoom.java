package org.jgl.opengl.test;

import static javax.media.opengl.GL.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.test.TGeometry.*;
import static org.jgl.math.vector.Vector2Ops.*;
import static org.jgl.math.vector.VectorOps.*;

import javax.media.opengl.GL3;

import org.jgl.math.angle.Angle;
import org.jgl.math.matrix.io.BufferedMatrix2;
import org.jgl.math.vector.*;
import org.jgl.opengl.*;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.glsl.attribute.GLUFloatMat2;
import org.jgl.opengl.util.GLViewSize;
import org.jgl.time.util.ExecutionState;

public class T006NewtonZoom extends GL3EventListener {

	private GLProgram p;
	private GLVertexArray rectVao = new GLVertexArray();
	private GLUFloatMat2 zoomMatrix;
	private BufferedMatrix2 m = new BufferedMatrix2();
	private Vector2 x = new Vector2();
	private Vector2 y = new Vector2();
	private Angle a = new Angle();
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("./src/test/resources/org/jgl/glsl/test/t006NewtonZoom/newtonZoom.vs", 
				"./src/test/resources/org/jgl/glsl/test/t006NewtonZoom/newtonZoom.fs", gl);

		rectVao.init(gl);
		p.bind(); {
			zoomMatrix = p.getMat2("ZoomMatrix");
			p.getStageAttribute("Position").set(rectVao, 
					buffer(rectangle_verts, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2), 
					false, 0).enable();
			p.getVec3("Color1").set(0.2f, 0.02f, 0.05f);
			p.getVec3("Color2").set(1.0f, 0.95f, 0.98f);
		}
		gl.glClearDepth(1);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		rectVao.bind();
		getDrawHelper().glClearColor().glClearDepth();
		gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		rectVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double uTime = currentState.getElapsedTimeSeconds();
		double scale = 1.0f / (3.0 * uTime + 1.0f);

		a.setFullCircles(uTime * 0.1);
		x.set(a.cos(), a.sin());
		perpendicular(x, y);
		scale(x, scale);
		scale(y, scale);

		m.m00 = x.x; m.m10 = x.y;
		m.m01 = y.x; m.m11 = y.y;
		zoomMatrix.set(m);
	}

	@Override
	protected void onResize(GL3 gl, GLViewSize newViewport) {
		getDrawHelper().glViewPort(newViewport);
	}
}