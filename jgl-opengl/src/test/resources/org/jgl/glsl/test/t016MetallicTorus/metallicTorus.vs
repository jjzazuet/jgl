#version 330

uniform mat4 ProjectionMatrix, CameraMatrix, ModelMatrix;

in vec4 Position;
in vec3 Normal;
out vec3 vertNormal;

void main(void) {
	vertNormal = mat3(CameraMatrix)*mat3(ModelMatrix)*Normal;
	gl_Position = 
		ProjectionMatrix *
		CameraMatrix *
		ModelMatrix *
		Position;
}