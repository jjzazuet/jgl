#version 330

uniform sampler2D CheckerTex;

in vec3 vertLightDir;
in vec3 vertNormal;
in vec3 vertColor;
in vec2 vertTexCoord;

out vec3 fragColor;

void main(void)
{
	float c = texture(CheckerTex, vertTexCoord).r;
	float l = pow(max(dot(vertLightDir, vertNormal)+0.1,0.0)*1.6,2.0);
	fragColor = mix(
		vec3(0, 0, 0),
		vertColor,
		mix(c, 1.0, 0.2)*(l+0.3)
	);
}