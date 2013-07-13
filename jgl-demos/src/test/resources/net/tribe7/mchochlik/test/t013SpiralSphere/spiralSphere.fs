#version 330

in vec2 vertTexCoord;
out vec4 fragColor;

void main(void) {
	
	float i = int((
		vertTexCoord.x+
		vertTexCoord.y 
	)*16) % 2;
	
	fragColor = mix(
		vec4(0.9, 0.9, 0.9, 1.0),
		vec4(1.0, 0.3, 0.4, 1.0),
		i
	);
}