#version 330

in vec3 vertNormal;
out vec4 fragColor;

void main(void) {
	fragColor = vec4(abs(vertNormal), 1.0);
}