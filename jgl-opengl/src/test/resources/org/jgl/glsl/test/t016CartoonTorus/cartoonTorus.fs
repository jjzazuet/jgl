#version 330

in vec3 vertNormal;
out vec4 fragColor;
uniform vec3 LightPos;

void main(void) {
	float intensity = 2.0 * max(
		dot(vertNormal,  LightPos)/
		length(LightPos),
		0.0
	);
	if(!gl_FrontFacing)
		fragColor = vec4(0.0, 0.0, 0.0, 1.0);
	else if(intensity > 0.9)
		fragColor = vec4(1.0, 0.9, 0.8, 1.0);
	else if(intensity > 0.1)
		fragColor = vec4(0.7, 0.6, 0.4, 1.0);
	else
		fragColor = vec4(0.3, 0.2, 0.1, 1.0);
}