package org.jgl.opengl.glsl;

import static org.jgl.opengl.util.GLBufferUtils.*;
import static org.jgl.opengl.util.GLSLUtils.*;
import static com.google.common.base.Throwables.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static org.jgl.os.io.IoUtils.*;
import static com.google.common.base.Preconditions.*;

import java.io.File;
import java.net.*;
import java.nio.*;
import java.nio.charset.*;

import org.jgl.opengl.GLContextBoundResource;

import com.google.common.base.Charsets;

public class GLShader extends GLContextBoundResource {
	
	private final GLShaderType type;
	private String shaderSourceText = null;
	private URL sourceLocation = null;

	private GLShader(GLShaderType t) { type = checkNotNull(t); }

	public GLShader(GLShaderType t, URL srcLocation) {
		this(t);
		sourceLocation = checkNotNull(srcLocation);
	}
	
	public GLShader(GLShaderType t, File srcFile) throws MalformedURLException {
		this(t);
		checkNotNull(srcFile);
		checkArgument(srcFile.exists());
		sourceLocation = srcFile.toURI().toURL();
	}
	
	public GLShader(GLShaderType t, String srcText) {
		this(t);
		checkNotNull(srcText);
		checkArgument(srcText.length() != 0);
		shaderSourceText = srcText;
	}

	private void compile() throws CharacterCodingException {
		
		IntBuffer b = intBuffer(getSource().length());
		String [] lines = new String [] {getSource()};

		getGl().glShaderSource(getGlResourceHandle(), lines.length, lines, b);
		getGl().glCompileShader(getGlResourceHandle());
		int compileStatus = getGlslParam(this, GL_COMPILE_STATUS);
		
		if (compileStatus == GL_FALSE) {
			throw new IllegalStateException(resourceMsg(getGlslLog(this)));
		}
	}
	
	@Override
	protected void doInit() { 
		try {
			setGlResourceHandle(
					getGl().glCreateShader(
							getType().getGlType()));
			compile();			
		} catch (Exception e) { propagate(e); }
	}
	
	public String getSource() {
		if (shaderSourceText == null) {
			shaderSourceText = readUrl(sourceLocation, Charsets.UTF_8);
		}
		return shaderSourceText;
	}

	@Override
	protected void doBind() { throw new IllegalStateException(); }
	@Override
	protected void doUnbind() { throw new IllegalStateException(); }
	@Override
	protected void doDestroy() { getGl().glDeleteShader(getGlResourceHandle()); }

	public GLShaderType getType() { return type; }

	@Override
	public boolean equals(Object o) {
		boolean equals = o != null && 
				o instanceof GLShader && 
				getSource().compareTo(((GLShader) o).getSource()) == 0;
		return equals;
	}
}