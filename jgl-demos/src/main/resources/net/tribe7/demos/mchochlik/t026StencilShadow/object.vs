#version 330

in vec4 Position;
in vec3 Normal;
uniform mat4 ProjectionMatrix, CameraMatrix, ModelMatrix;
uniform vec3 LightPos;
out vec3 vertNormal;
out vec3 vertLight;

void main(void) {
	gl_Position = ModelMatrix * Position;
	vertNormal = mat3(ModelMatrix)*Normal;
	vertLight = LightPos - gl_Position.xyz;
	gl_Position = ProjectionMatrix * CameraMatrix * gl_Position;
}