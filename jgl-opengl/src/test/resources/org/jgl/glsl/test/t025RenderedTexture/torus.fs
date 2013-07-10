#version 330

in vec3 vertNormal;
in vec3 vertLight;
in vec2 vertTexCoord;
out vec4 fragColor;

void main(void) {
	float d = dot(
		vertNormal, 
		normalize(vertLight)
	);
	float i = (
		int(vertTexCoord.x*18) % 2+
		int(vertTexCoord.y*14) % 2
	) % 2;
	float c = (0.4 + max(d, 0.0))*(1-i/2);
	fragColor = vec4(c, c, c, 1.0);
}