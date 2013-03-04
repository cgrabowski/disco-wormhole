#pragma optionNV unroll all

uniform mat4 u_modelViewProjection;
uniform mat4 u_modelView;
// uniform vec3 u_lightPos[50];
uniform vec3 u_lightPos;

attribute vec4 a_position;
attribute vec2 a_texCoord0;
//attribute vec2 a_texCoord0;
attribute vec3 a_normal;

varying vec2 v_texCoord;
//varying float v_diffuse[50];
varying float v_diffuse;
varying vec4 v_position;
varying vec3 v_modelViewPosition;

void main()
{
// never give up!!!!!

// transform the vertex into eye space
vec3 modelViewVertex = vec3(u_modelView * a_position);
// transform the normal's orientaiton into eye space
vec3 modelViewNormal = vec3(u_modelView * vec4(a_normal, 0.0));

for(int i = 0; i < 50; i++) {

	// will get used for attenuation
	float dist = length(u_lightPos - modelViewVertex);
	// get a lighting direction vector from the light to the vertex
	vec3 lightVector = normalize(u_lightPos - modelViewVertex);
	// calculate the dot product of the light vector and vertex normal.
	// If the normal and light vector are pointing in the same direction
	// then it will get max illumination
	v_diffuse = max(dot(modelViewNormal, lightVector), 0.1) / (dist / 2.0);
	// attenuate the light based on distance

}

// mul vertex by MVP to get the final point in normalized screen coords
gl_Position = u_modelViewProjection * a_position;
v_texCoord = a_texCoord0;
v_position = u_modelViewProjection * a_position;
v_modelViewPosition = modelViewVertex;
}   
