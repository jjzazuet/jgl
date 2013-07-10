#version 330

uniform sampler2D TexUnit;
in vec3 vertNormal;
in vec3 vertLight;
in vec2 vertTexCoord;
out vec4 fragColor;

void main(void) {
	float l = sqrt(length(vertLight));
	float d = l > 0? dot(vertNormal, normalize(vertLight)) / l : 0.0;
	float i = 0.6 + max(d, 0.0);
	fragColor = texture(TexUnit, vertTexCoord)*i;
}