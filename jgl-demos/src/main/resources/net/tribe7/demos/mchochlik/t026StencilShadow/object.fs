#version 330

in vec3 vertNormal;
in vec3 vertLight;
uniform vec3 Color;
uniform float LightMult;
out vec4 fragColor;

void main(void) {
	float l = sqrt(length(vertLight));
	float d = l > 0.0 ?
		dot(
			vertNormal,
			normalize(vertLight)
		) / l : 0.0;
	float i = 0.3 + max(d, 0.0) * LightMult;
	fragColor = vec4(Color*i, 1.0);
}