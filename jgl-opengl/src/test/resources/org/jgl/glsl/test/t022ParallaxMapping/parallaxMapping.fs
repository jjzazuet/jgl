#version 330

uniform sampler2D BumpTex;
uniform int BumpTexWidth;
uniform int BumpTexHeight;

float DepthMult = 0.1;

in vec3 vertEye;
in vec3 vertLight;
in vec3 vertNormal;
in vec2 vertTexCoord;
in vec3 vertViewTangent;
in mat3 NormalMatrix;
out vec4 fragColor;

void main(void) {

	vec3 ViewTangent = normalize(vertViewTangent);
	float perp = -dot(normalize(vertEye), vertNormal);
	float sampleInterval = 1.0/length(
		vec2(BumpTexWidth, BumpTexHeight)
	);
	vec3 sampleStep = ViewTangent*sampleInterval;
	float prevD = 0.0;
	float depth = texture(BumpTex, vertTexCoord).w;
	float maxOffs = min((depth*DepthMult)/(-ViewTangent.z), 1.0);
	vec3 viewOffs = vec3(0.0, 0.0, 0.0);
	vec2 offsTexC = vertTexCoord + viewOffs.xy;

	while(length(viewOffs) < maxOffs) {
		
		if(offsTexC.x <= 0.0 || offsTexC.x >= 1.0)
			break;
		if(offsTexC.y <= 0.0 || offsTexC.y >= 1.0)
			break;
		if(depth*DepthMult*perp <= -viewOffs.z)
			break;
		
		viewOffs += sampleStep;
		offsTexC = vertTexCoord + viewOffs.xy;
		prevD = depth;
		depth = texture(BumpTex, offsTexC).w;
	}
	
	offsTexC = vec2(
		clamp(offsTexC.x, 0.0, 1.0),
		clamp(offsTexC.y, 0.0, 1.0) 
	);
	
	float b = (
		1 +
		int(offsTexC.x*16) % 2+
		int(offsTexC.y*16) % 2
	) % 2;
	
	vec3 c = vec3(b, b, b);
	vec3 n = texture(BumpTex, offsTexC).xyz;
	vec3 finalNormal = NormalMatrix * n;
	float l = length(vertLight);
	float d = (l > 0.0) ? dot(
		normalize(vertLight), 
		finalNormal
	) / l : 0.0;
	float i = 0.1 + 2.5*max(d, 0.0);
	fragColor = vec4(c*i, 1.0);
}