#version 330

uniform mat4 ProjectionMatrix, CameraMatrix;
in vec4 Position;
in vec3 Normal;
out vec3 vertNormal;

void main(void) {
	vertNormal = Normal;
	gl_Position = ProjectionMatrix * CameraMatrix * Position;
}