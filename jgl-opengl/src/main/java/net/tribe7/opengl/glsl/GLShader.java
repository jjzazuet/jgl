package net.tribe7.opengl.glsl;

import static net.tribe7.opengl.util.GLBufferUtils.*;
import static net.tribe7.opengl.util.GLIOUtils.*;
import static net.tribe7.opengl.util.GLSLUtils.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;
import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.math.Preconditions.*;

import java.io.File;
import java.net.*;
import java.nio.*;
import net.tribe7.opengl.GLContextBoundResource;
import net.tribe7.common.base.Charsets;

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
		checkArgument(srcText.length() != ZERO);
		shaderSourceText = srcText;
	}

	@Override
	protected int doInit() { 
		IntBuffer b = intBuffer(getSource().length());
		String [] lines = new String [] {getSource()};

		setGlResourceHandle(getGl().glCreateShader(getType().getGlType()));
		getGl().glShaderSource(getGlResourceHandle(), lines.length, lines, b);
		checkError();
		getGl().glCompileShader(getGlResourceHandle());
		checkError();

		int compileStatus = getGlslParam(this, GL_COMPILE_STATUS);
		checkState(compileStatus != GL_FALSE, resourceMsg(getGlslLog(this)));
		return getGlResourceHandle();
	}

	public String getSource() {
		if (shaderSourceText == null) {
			shaderSourceText = readUrl(sourceLocation, Charsets.UTF_8);
		}
		return shaderSourceText;
	}

	@Override
	protected void doBind() { throw new UnsupportedOperationException(); }
	@Override
	protected void doUnbind() { throw new UnsupportedOperationException(); }
	@Override
	protected void doDestroy() { getGl().glDeleteShader(getGlResourceHandle()); }

	public GLShaderType getType() { return type; }

	@Override
	public boolean equals(Object o) {
		boolean equals = o != null && 
				o instanceof GLShader && 
				getSource().compareTo(((GLShader) o).getSource()) == ZERO;
		return equals;
	}
}