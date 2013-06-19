#version 330

in vec3 vertColor;
in vec3 vertNormal;
out vec4 fragColor;

void main(void) {
	float intensity = pow(
		dot(
			vertNormal,
			vec3(0.0, 0.0, 1.0)
		), 3.0
	);
	fragColor = vec4(vertColor,1.0)*intensity;
}