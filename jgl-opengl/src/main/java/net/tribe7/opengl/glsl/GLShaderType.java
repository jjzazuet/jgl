package net.tribe7.opengl.glsl;

import static javax.media.opengl.GL3.*;

public enum GLShaderType {

	VERTEX_SHADER(GL_VERTEX_SHADER), 
	FRAGMENT_SHADER(GL_FRAGMENT_SHADER),
	GEOMETRY_SHADER(GL_GEOMETRY_SHADER);
	
	private int glType;
	private GLShaderType(int glType) { this.glType = glType;} 
	public int getGlType() { return glType; }
}