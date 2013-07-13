package net.tribe7.opengl.util;

import static net.tribe7.common.base.Preconditions.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.opengl.glsl.GLShaderType.*;

import java.io.File;
import java.net.URL;
import java.nio.*;

import javax.media.opengl.GL3;

import net.tribe7.opengl.*;
import net.tribe7.opengl.glsl.*;

import org.slf4j.*;

import net.tribe7.common.base.Charsets;

public class GLSLUtils {

	private static final Logger log = LoggerFactory.getLogger(GLSLUtils.class);

	public static final GLProgram loadProgram(String vsPath, String fsPath, GL3 gl) throws Exception {

		checkNotNull(vsPath);
		checkNotNull(fsPath);
		checkNotNull(gl);

		GLShader vs = loadVertexShader(vsPath);
		GLShader fs = loadFragmentShader(fsPath);
		GLProgram p = new GLProgram();

		p.attachShader(vs).attachShader(fs).init(gl);
		return p;
	}

	public static final GLProgram loadProgram(String vsPath, String gsPath, String fsPath, GL3 gl) throws Exception {

		checkNotNull(vsPath);
		checkNotNull(fsPath);
		checkNotNull(gsPath);
		checkNotNull(gl);

		GLShader vs = loadVertexShader(vsPath);
		GLShader fs = loadFragmentShader(fsPath);
		GLShader gs = loadGeometryShader(gsPath);
		GLProgram p = new GLProgram();

		p.attachShader(vs).attachShader(fs).attachShader(gs).init(gl);
		return p;
	}

	public static final GLShader loadShader(String path, GLShaderType t) throws Exception {

		checkNotNull(path);
		File shaderSrcFile = new File(path);
		GLShader s = null;

		if (shaderSrcFile.exists()) {
			s = new GLShader(t, shaderSrcFile);
		} else {
			URL shaderClassPathResource = GLSLUtils.class.getResource(path);
			s = new GLShader(t, shaderClassPathResource);
		}

		checkNotNull(s);
		return s;
	}

	public static final GLShader loadVertexShader(String vsPath) throws Exception {
		return loadShader(vsPath, VERTEX_SHADER);
	}

	public static final GLShader loadFragmentShader(String fsPath) throws Exception {
		return loadShader(fsPath, FRAGMENT_SHADER);
	}

	public static final GLShader loadGeometryShader(String gsPath) throws Exception {
		return loadShader(gsPath, GEOMETRY_SHADER);
	}

	public static final int getGlslParam(GLContextBoundResource r, int param) {

		checkArgument(r instanceof GLShader || r instanceof GLProgram);
		IntBuffer b = IntBuffer.allocate(1);

		if (r instanceof GLShader) {
			r.getGl().glGetShaderiv(r.getGlResourceHandle(), param, b);
		} else if (r instanceof GLProgram) {
			r.getGl().glGetProgramiv(r.getGlResourceHandle(), param, b);
		}
		r.checkError();
		return b.get();
	}

	public static final String getGlslLog(GLContextBoundResource r) {

		checkArgument(r instanceof GLShader || r instanceof GLProgram);

		int logLength = getGlslParam(r, GL_INFO_LOG_LENGTH);
		String glslLog = "[???]";
		ByteBuffer logData = ByteBuffer.allocate(logLength + 1);

		if (r instanceof GLShader) {
			r.getGl().glGetShaderInfoLog(r.getGlResourceHandle(), logLength, null, logData);
		} else if (r instanceof GLProgram) {
			r.getGl().glGetProgramInfoLog(r.getGlResourceHandle(), logLength, null, logData);
		}

		r.checkError();

		try {
			glslLog = Charsets.UTF_8.newDecoder().decode(logData).toString();
		} catch (Exception e) { 
			log.error(r.resourceMsg("Unable to retrieve GL resource error log"), e); 
		}

		return glslLog;
	}
}