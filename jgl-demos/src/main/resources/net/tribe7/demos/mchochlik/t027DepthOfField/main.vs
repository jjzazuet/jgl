#version 330

uniform mat4 ProjectionMatrix, CameraMatrix, ModelMatrix;
uniform vec3 LightPos;
in vec4 Position;
in vec3 Normal;
out vec3 vertLightDir;
out vec3 vertNormal;

void main(void) {
	gl_Position = ModelMatrix * Position;
	vertLightDir = normalize(LightPos - gl_Position.xyz);
	vertNormal = normalize(mat3(ModelMatrix)*Normal);
	gl_Position = ProjectionMatrix * CameraMatrix * gl_Position;
}
