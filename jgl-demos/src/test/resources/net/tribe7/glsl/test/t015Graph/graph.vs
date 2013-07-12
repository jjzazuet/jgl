#version 330

uniform mat4 ProjectionMatrix, CameraMatrix;
in vec4 Position;

void main(void) {
	gl_Position = 
		ProjectionMatrix *
		CameraMatrix *
		Position;
	gl_PointSize = 4.0 * gl_Position.w / gl_Position.z;
}