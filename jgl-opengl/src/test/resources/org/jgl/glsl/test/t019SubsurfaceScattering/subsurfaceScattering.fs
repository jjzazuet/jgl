#version 330

in float vertMult;
in vec3 vertColor;
in vec3 vertWrapNormal;
in vec3 vertNormal;
in vec3 vertLight;
out vec4 fragColor;
uniform int InstCount;

void main(void) {
	float l = dot(vertLight, vertLight);
	float d = l > 0.0 ? dot(
		vertNormal, 
		normalize(vertLight)
	) / l : 0.0;
	float s = max(
		dot(vertWrapNormal, vertLight)/l,
		0.0
	);
	float intensity = clamp(
		0.2 + d * 3.0 + s * 5.5,
		0.0,
		1.0
	);
	fragColor = vec4(
		abs(vertColor) * intensity,
		(2.5 + 1.5*d + 1.5*s) / InstCount
	);
}