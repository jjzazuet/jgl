#version 330

in vec2 vertTexCoord;
out vec4 fragColor;

void main(void) {
	float i = int((vertTexCoord.x + vertTexCoord.y)*8) % 2;
	fragColor = mix(
		vec4(0, 0, 0, 1),
		vec4(1, 1, 0, 1),
		i
	);
}