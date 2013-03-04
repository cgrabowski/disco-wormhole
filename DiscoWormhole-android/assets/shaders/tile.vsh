uniform mat4 u_modelViewProjection;
uniform mat4 u_modelView;
//uniform vec3 u_eyePos;
uniform mat4 u_normalMatrix;
uniform vec3 u_lightsPos[2];
//uniform vec4 u_color;

attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec3 a_normal;

varying vec2 v_texCoord;
//varying vec3 v_eyeVec;
varying vec3 v_lightVec1;
varying vec3 v_lightVec2;
//varying vec4 v_color;

#ifdef GL_ES
varying highp float v_lightDistance1;
varying highp float v_lightDistance2;
#else
varying float v_lightDistance1;
varying float v_lightDistance2;
#endif

vec3 findTangent(vec3 normal)
{
	vec3 T;
	vec3 c1 = cross(normal, vec3(0.0, 0.0, 1.0));
	vec3 c2 = cross(normal, vec3(0.0, 1.0, 0.0));
	if(length(c1) > length(c2))
	{
		T = c1;
	} else {
		T = c2;
	}
	return T;
}

void main()
{
	//v_color = u_color;
	v_texCoord = a_texCoord0;
	gl_Position = u_modelViewProjection * a_position;

	vec3 N = normalize(u_normalMatrix * vec4(a_normal, 0.0)).xyz;
	vec3 T = normalize(u_normalMatrix * vec4(findTangent(N), 0.0)).xyz;
	vec3 B = cross(N, T);

	vec3 mvPos = vec3(u_modelView * a_position);
	//v_eyeVec.x = dot(u_eyePos, T);
	//v_eyeVec.y = dot(u_eyePos, B);
	//v_eyeVec.z = dot(u_eyePos, N);
	
	vec3 mvLightPos = u_lightsPos[0] - mvPos;
	v_lightVec1 = vec3(dot(mvLightPos, T), dot(mvLightPos, B), dot(mvLightPos, N));
	v_lightDistance1 = distance(mvPos, u_lightsPos[0]);

	mvLightPos = u_lightsPos[1] - mvPos;
	v_lightVec2 = vec3(dot(mvLightPos, T), dot(mvLightPos, B), dot(mvLightPos, N));
	v_lightDistance2 = distance(mvPos, u_lightsPos[1]);
}
