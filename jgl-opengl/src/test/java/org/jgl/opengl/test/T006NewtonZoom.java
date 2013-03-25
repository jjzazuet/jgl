package org.jgl.opengl.test;

import static java.lang.Math.*;
import static org.jgl.math.angle.AngleOps.*;
import static javax.media.opengl.GL.*;
import static org.jgl.opengl.GLBufferFactory.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static org.jgl.opengl.test.TGeometry.*;
import static org.jgl.math.matrix.Matrix2Ops.*;
import static org.jgl.math.vector.Vector2Ops.*;
import static org.jgl.math.vector.VectorOps.*;

import java.nio.FloatBuffer;

import javax.media.opengl.GL3;

import org.jgl.math.matrix.Matrix2;
import org.jgl.math.vector.*;
import org.jgl.opengl.*;
import org.jgl.opengl.util.GlViewSize;
import org.jgl.time.util.ExecutionState;

public class T006NewtonZoom extends GL3EventListener {

	private GLProgram p;
	private GLVertexArray rectVao = new GLVertexArray();
	private GLUniformAttribute zoomMatrix;
	private Matrix2 m = new Matrix2();
	private Vector2 x = new Vector2();
	private Vector2 y = new Vector2();
	private FloatBuffer matBuffer = FloatBuffer.allocate(Matrix2.COMPONENT_SIZE);
	
	@Override
	protected void doInit(GL3 gl) throws Exception {

		p = loadProgram("../jgl-opengl/src/test/resources/org/jgl/glsl/test/t006NewtonZoom/newtonZoom.vs", 
				"../jgl-opengl/src/test/resources/org/jgl/glsl/test/t006NewtonZoom/newtonZoom.fs", gl);
		
		GLVertexAttribute position = p.getStageAttribute("Position");
		zoomMatrix = p.getUniformAttribute("ZoomMatrix");
		
		p.bind();
		rectVao.init(gl);
		rectVao.bindAttribute(position, buffer(rectangle_verts, gl, GL_ARRAY_BUFFER, GL_STATIC_DRAW, 2), 0).enable(position);
		p.getUniformAttribute("Color1").setVec3f(0.2f, 0.02f, 0.05f);
		p.getUniformAttribute("Color2").setVec3f(1.0f, 0.95f, 0.98f);
		gl.glClearDepth(1);
	}

	@Override
	protected void doRender(GL3 gl, ExecutionState currentState) throws Exception {
		rectVao.bind();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		rectVao.unbind();
	}

	@Override
	protected void doUpdate(GL3 gl, ExecutionState currentState) throws Exception {

		double uTime = currentState.elapsedTimeUs * 0.000001;
		double scale = 1.0f / (3.0 * uTime + 1.0f);
		double angle = fullCircles(uTime * 0.1);
		
		x.set(cos(angle), sin(angle));
		perpendicular(x, y);
		scale(x, scale);
		scale(y, scale);
		
		m.m00 = x.x; m.m01 = x.y;
		m.m10 = y.x; m.m11 = y.y;
		
		store(matBuffer, m);
		zoomMatrix.setMat2fv(true, matBuffer, m);
	}

	@Override
	protected void onResize(GL3 gl, GlViewSize newViewport) {
		gl.glViewport(newViewport.x, newViewport.y, newViewport.width, newViewport.height);
	}
}