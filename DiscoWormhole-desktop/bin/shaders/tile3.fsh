#ifdef GL_ES
precision mediump float;
#endif

#pragma optionNV unroll all

uniform mat4 u_modelViewProjection;
uniform mat4 u_modelView;
//uniform vec3 u_lightPos[50];
uniform vec3 u_lightPos;

uniform sampler2D u_texture;
uniform sampler2D u_normals;
//uniform vec4 u_lightColor[50];
uniform vec4 u_lightColor;
uniform vec4 u_color;

varying vec2 v_texCoord;
//varying float v_diffuse[50];
varying float v_diffuse;
varying vec4 v_position;
varying vec3 v_modelViewPosition;

void main()
{

vec4 normal = texture2D(u_normals, v_texCoord);
//normals need to be converted to [-1.0, 1.0] range and normalized
normal = normalize(normal * 2.0 - 1.0);

// transform the normal's orientaiton into eye space
vec3 modelViewNormal = vec3(u_modelView * normal);

vec4 color;// = texture2D(u_texture, v_texCoord);

//for (int i = 0; i < 50; i++) {
//int i = 10;

	float dist = length(u_lightPos - v_modelViewPosition);
	vec3 lightVector = normalize(u_lightPos - v_modelViewPosition);
	float diffuse = max(dot(modelViewNormal, lightVector), 0.0) / (dist * 20);

//	color += v_diffuse[i] * u_lightColor[i] * normals;
	color += diffuse * u_lightColor;
		
//}

// gl_FragColor = v_color * texture2D(u_texture, v_texCoord) * normals;
gl_FragColor = color ;//+ vec4(u_color.r / 4, u_color.g / 4, u_color.b / 4, 1.0) + texture2D(u_texture, v_texCoord) / 4;
}