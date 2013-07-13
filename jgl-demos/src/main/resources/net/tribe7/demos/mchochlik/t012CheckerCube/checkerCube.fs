#version 330

in vec2 vertTexCoord;
out vec4 fragColor;

void main(void) {
	float i = (1 + int(vertTexCoord.x*8) % 2 + int(vertTexCoord.y*8) % 2) % 2;
	fragColor = vec4(i, i, i, 1.0);
}