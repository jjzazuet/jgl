#version 330

in vec3 vertColor;
in vec3 vertNormal;
in vec3 vertLight;
out vec4 fragColor;

void main(void) {
	float l = length(vertLight);
	float d = l != 0? dot(
		normalize(vertNormal), 
		normalize(vertLight)
	) / l : 0.0;
	float i = 0.1 + 4.2*max(d, 0.0);
	fragColor = vec4(vertColor*i, 1.0);
}