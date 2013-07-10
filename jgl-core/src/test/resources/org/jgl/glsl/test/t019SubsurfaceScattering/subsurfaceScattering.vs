#version 330

uniform mat4 ProjectionMatrix, CameraMatrix, ModelMatrix;
uniform vec3 LightPos;
uniform int InstCount;
uniform int FrontFacing;
in vec4 Position;
in vec3 Normal;
out float vertMult;
out vec3 vertColor;
out vec3 vertWrapNormal;
out vec3 vertNormal;
out vec3 vertLight;

void main(void) {
	
	int inst = (FrontFacing != 0) ? 
		(InstCount - gl_InstanceID - 1):
		gl_InstanceID;
	vertMult = float(inst) / float(InstCount-1);
	float sca = 1.0 - 0.3 * pow(vertMult, 2);
	
	mat4 ScaleMatrix = mat4(
		sca, 0.0, 0.0, 0.0,
		0.0, sca, 0.0, 0.0,
		0.0, 0.0, sca, 0.0,
		0.0, 0.0, 0.0, 1.0 
	);
	
	gl_Position = ModelMatrix * Position;
	vertColor = Normal;
	vec3 wrap = Position.xyz - Normal;
	
	vertWrapNormal = 
		mat3(ModelMatrix)*
		normalize(mix(
			Normal,
			wrap,
			mix(0.5, 1.0, vertMult)
		));
		
	vertNormal = mat3(ModelMatrix)*Normal;
	vertLight = LightPos-gl_Position.xyz;
	
	gl_Position = 
		ProjectionMatrix *
		CameraMatrix *
		ScaleMatrix *
		gl_Position;
}