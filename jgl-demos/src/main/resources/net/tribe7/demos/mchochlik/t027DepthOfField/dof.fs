#version 330

uniform sampler2DRect ColorTex;
uniform sampler2DRect DepthTex;
uniform float FocusDepth;
uniform uint SampleMult;
in vec2 vertTexCoord;
out vec4 fragColor;
const float strength = 16.0;

void main(void) {
	float fragDepth = texture(DepthTex, vertTexCoord).r;
	vec3 color = texture(ColorTex, vertTexCoord).rgb;
	float of = abs(fragDepth - FocusDepth);
	int nsam = int(of*SampleMult);
	float inv_nsam = 1.0 / (1.0 + nsam);
	float astep = (3.14151*4.0)/nsam;
	for(int i=0; i!=nsam; ++i)
	{
		float a = i*astep;
		float d = sqrt(i*inv_nsam);
		float sx = cos(a)*of*strength*d;
		float sy = sin(a)*of*strength*d;
		vec2 samTexCoord = vertTexCoord + vec2(sx, sy) + noise2(vec2(sx, sy));
		color += texture(ColorTex, samTexCoord).rgb;
	}
	fragColor = vec4(color * inv_nsam , 1.0);
}