package org.jgl.opengl.util;

import static com.google.common.base.Preconditions.*;
import static javax.media.opengl.GL2.*;
import static org.jgl.opengl.glsl.GLShaderType.*;

import java.io.File;
import java.nio.*;

import javax.media.opengl.GL3;

import org.jgl.opengl.*;
import org.jgl.opengl.glsl.GLProgram;
import org.jgl.opengl.glsl.GLShader;
import org.jgl.opengl.glsl.GLShaderType;
import org.slf4j.*;

import com.google.common.base.Charsets;

public class GLSLUtils {

	private static final Logger log = LoggerFactory.getLogger(GLSLUtils.class);

	public static final GLProgram loadProgram(String vsPath, String fsPath, GL3 gl) throws Exception {
		
		checkNotNull(vsPath);
		checkNotNull(fsPath);
		checkNotNull(gl);
				
		GLShader vs = loadShader(vsPath, VERTEX_SHADER);
		GLShader fs = loadShader(fsPath, FRAGMENT_SHADER);
		GLProgram p = new GLProgram();
		
		p.attachShader(vs).attachShader(fs).init(gl);		
		return p;
	}

	public static final GLProgram loadProgram(String vsPath, String gsPath, String fsPath, GL3 gl) throws Exception {
		
		checkNotNull(vsPath);
		checkNotNull(fsPath);
		checkNotNull(gsPath);
		checkNotNull(gl);

		GLShader vs = loadShader(vsPath, VERTEX_SHADER);
		GLShader fs = loadShader(fsPath, FRAGMENT_SHADER);
		GLShader gs = loadShader(gsPath, GEOMETRY_SHADER);
		GLProgram p = new GLProgram();
		
		p.attachShader(vs).attachShader(fs).attachShader(gs).init(gl);		
		return p;
	}

	public static final GLShader loadShader(String path, GLShaderType t) throws Exception {

		checkNotNull(path);
		File shaderSrcFile = new File(path);
		checkArgument(shaderSrcFile.exists());
		GLShader s = new GLShader(t, shaderSrcFile);

		return s;
	}

	public static final int getGlslParam(GLContextBoundResource r, int param) {

		checkArgument(r instanceof GLShader || r instanceof GLProgram);
		IntBuffer b = IntBuffer.allocate(1);
		
		if (r instanceof GLShader) {
			r.getGl().glGetShaderiv(r.getGlResourceHandle(), param, b);			
		} else if (r instanceof GLProgram) {
			r.getGl().glGetProgramiv(r.getGlResourceHandle(), param, b);
		}
		
		return b.get();
	}
	
	public static final String getGlslLog(GLContextBoundResource r) {

		checkArgument(r instanceof GLShader || r instanceof GLProgram);
		
		int logLength = getGlslParam(r, GL_INFO_LOG_LENGTH);
		String glslLog = "[]";
		ByteBuffer logData = ByteBuffer.allocate(logLength + 1);
		
		if (r instanceof GLShader) {
			r.getGl().glGetShaderInfoLog(r.getGlResourceHandle(), logLength, null, logData);			
		} else if (r instanceof GLProgram) {
			r.getGl().glGetProgramInfoLog(r.getGlResourceHandle(), logLength, null, logData);
		}

		try {
			glslLog = Charsets.UTF_8.newDecoder().decode(logData).toString();
		} catch (Exception e) { 
			log.error(r.resourceMsg("Unable to retrieve GL resource error log"), e); 
		}
		
		return glslLog;
	}
}