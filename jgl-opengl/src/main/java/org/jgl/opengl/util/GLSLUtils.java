package org.jgl.opengl.util;

import static javax.media.opengl.GL2.*;
import static com.google.common.base.Preconditions.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.jgl.opengl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

public class GLSLUtils {

	private static final Logger log = LoggerFactory.getLogger(GLSLUtils.class);
	
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
