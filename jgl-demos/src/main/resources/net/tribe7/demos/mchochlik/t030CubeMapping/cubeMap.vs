#version 330

uniform vec4 LightPos;
uniform vec3 Offset;
in vec4 Position;
in vec3 Normal;
out vec3 tempColor;
out vec3 tempNormal;
out vec3 tempLight;

void main(void) {
	gl_Position = vec4(Position.xyz+Offset, 1.0);
	tempColor = normalize(
		vec3(1.0, 1.0, 1.0) -
		normalize(Normal + Offset)
	);
	tempNormal = Normal;
	tempLight = LightPos.xyz - gl_Position.xyz;
}