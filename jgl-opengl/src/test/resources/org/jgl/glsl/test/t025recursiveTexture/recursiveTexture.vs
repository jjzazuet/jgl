#version 330

uniform mat4 ProjectionMatrix, CameraMatrix, ModelMatrix;
in vec4 Position;
in vec3 Normal;
in vec2 TexCoord;
out vec3 vertNormal;
out vec3 vertLight;
out vec2 vertTexCoord;
uniform vec3 LightPos;

void main(void) {
	vertNormal = mat3(ModelMatrix)*Normal;
	gl_Position = ModelMatrix * Position;
	vertLight = LightPos - gl_Position.xyz;
	vertTexCoord = TexCoord;
	gl_Position = ProjectionMatrix * CameraMatrix * gl_Position;
}