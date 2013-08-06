#version 330

uniform sampler2DRect CurrentFrame;
uniform sampler2DRect PreviousFrames;
uniform float Splitter;

in vec2 vertTexCoord;

out vec3 fragColor;

vec3 sharp(void) {
	return texture(CurrentFrame, vertTexCoord).rgb;
}

vec3 blurred(void) {
	vec3 prev = vec3(0, 0, 0);
	const vec2 otc[9] = vec2[9](
		vec2(-1,-1),
		vec2( 0,-1),
		vec2( 1,-1),
		vec2(-1, 0),
		vec2( 0, 0),
		vec2( 1, 0),
		vec2(-1, 1),
		vec2( 0, 1),
		vec2( 1, 1) 
	);
	const float is = 1.0/9.0;
	for(int s=0; s!=9; ++s) {
		vec2 tc = vertTexCoord+otc[s]*2;
		prev += texture(PreviousFrames, tc).rgb * is;
	}
	vec3 curr = texture(CurrentFrame, vertTexCoord).rgb;
	float a = curr.x+curr.y+curr.z;
	return mix(curr, prev, 0.95-0.4*a);
}

void main(void) {
	if(gl_FragCoord.x < Splitter-1)
		fragColor = sharp();
	else if(gl_FragCoord.x > Splitter+1)
		fragColor = blurred();
	else fragColor = vec3(1.0, 1.0, 1.0);
}