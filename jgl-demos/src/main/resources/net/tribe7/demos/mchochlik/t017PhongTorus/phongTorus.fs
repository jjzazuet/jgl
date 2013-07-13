#version 330

in vec3 vertNormal;
in vec3 vertViewDir;
out vec4 fragColor;
uniform vec3 LightPos[3];

void main(void) {
	
	float amb = 0.2;
	float diff = 0.0;
	float spec = 0.0;
	
	for(int i=0;i!=3;++i) {
		diff += max(
			dot(vertNormal,  LightPos[i])/
			dot(LightPos[i], LightPos[i]),
			0.0
		);
		float k = dot(vertNormal, LightPos[i]);
		vec3 r = 2.0*k*vertNormal - LightPos[i];
		spec += pow(max(
			dot(normalize(r), vertViewDir),
			0.0
		), 32.0 * dot(r, r));
	}
	fragColor = 
		vec4(1.0, 0.1, 0.3, 1.0)*(amb+diff)+
		vec4(1.0, 1.0, 1.0, 1.0)*spec;
}