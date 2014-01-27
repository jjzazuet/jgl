#version 330

layout(triangles) in;
layout(triangle_strip, max_vertices = 12) out;

in float ld[];

uniform mat4 CameraMatrix, ProjectionMatrix;
uniform vec3 LightPos;

void main(void)
{
	for(int v=0; v!=3; ++v)
	{
		int a = v, b = (v+1)%3, c = (v+2)%3;
		vec4 pa = gl_in[a].gl_Position;
		vec4 pb = gl_in[b].gl_Position;
		vec4 pc = gl_in[c].gl_Position;
		vec4 px, py;
		if(ld[a] == 0.0 && ld[b] == 0.0)
		{
			px = pa;
			py = pb;
		}
		else if(ld[a] > 0.0 && ld[b] < 0.0)
		{
			float x = ld[a]/(ld[a]-ld[b]);
			float y;
			px = mix(pa, pb, x);
			if(ld[c] < 0.0)
			{
				y = ld[a]/(ld[a]-ld[c]);
				py = mix(pa, pc, y);
			}
			else
			{
				y = ld[c]/(ld[c]-ld[b]);
				py = mix(pc, pb, y);
			}
		}
		else continue;
		vec3 vx = px.xyz - LightPos;
		vec3 vy = py.xyz - LightPos;
		vec4 sx = vec4(px.xyz + vx*10.0, 1.0);
		vec4 sy = vec4(py.xyz + vy*10.0, 1.0);
		vec4 cpx = CameraMatrix * px;
		vec4 cpy = CameraMatrix * py;
		vec4 csx = CameraMatrix * sx;
		vec4 csy = CameraMatrix * sy;
		gl_Position = ProjectionMatrix * cpy;
		EmitVertex();
		gl_Position = ProjectionMatrix * cpx;
		EmitVertex();
		gl_Position = ProjectionMatrix * csy;
		EmitVertex();
		gl_Position = ProjectionMatrix * csx;
		EmitVertex();
		EndPrimitive();
		break;
	}
}
