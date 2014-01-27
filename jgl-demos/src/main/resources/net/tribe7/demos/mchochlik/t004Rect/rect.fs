#version 330
const float radius = 0.4;
in vec2 vertCoord;
uniform vec2 RedCenter, GreenCenter, BlueCenter;
out vec4 fragColor;
void main(void)
{
	vec3 dist = vec3(
		distance(vertCoord, RedCenter),
		distance(vertCoord, GreenCenter),
		distance(vertCoord, BlueCenter)
	);
	fragColor = vec4(
		dist.r < radius ? 1.0 : (2*radius - dist.r) / radius,
		dist.g < radius ? 1.0 : (2*radius - dist.g) / radius,
		dist.b < radius ? 1.0 : (2*radius - dist.b) / radius,
		1.0
	);
}