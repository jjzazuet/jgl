#version 330

uniform mat4 ProjectionMatrix, CameraMatrix;
uniform vec4 LightPos;
uniform vec3 Offset;
in vec4 Position;
in vec3 Normal;
out vec3 vertColor;
out vec3 vertNormal;
out vec3 vertLight;

void main(void) {
	gl_Position = vec4(Position.xyz+Offset, 1.0);
	vertColor = normalize(
		vec3(1.0, 1.0, 1.0) -
		normalize(Normal + Offset)
	);
	vertNormal = Normal;
	vertLight = LightPos.xyz - gl_Position.xyz;
	gl_Position = 
		ProjectionMatrix *
		CameraMatrix *
		gl_Position;
}