#version 330

uniform mat4 CameraMatrix;
uniform samplerCube CubeTex;
in vec3 vertNormal;
in vec3 vertLight;
in vec3 vertRefl;
out vec4 fragColor;

void main(void) {
	float l = length(vertLight);
	float d = l != 0? dot(
		normalize(vertNormal),
		normalize(vertLight)
	) / l : 0.0;
	float e = pow(dot(
		normalize(vertRefl),
		normalize(vertLight)
	), 32.0);
	vec3 color = texture(
		CubeTex,
		normalize(vertRefl)
	).rgb;
	fragColor = vec4(
		(0.3  + max(d,0.0)*0.5) * color +
		(0.05 + max(e,0.0))*vec3(1.0, 1.0, 1.0),
		1.0
	);
}