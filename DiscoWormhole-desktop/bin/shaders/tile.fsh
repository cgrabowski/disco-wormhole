#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform sampler2D u_normals;
uniform vec4 u_color;
//uniform vec4 u_lightsColor[2];

varying vec2 v_texCoord;
//varying vec3 v_eyeVec;
varying vec3 v_lightVec1;
varying vec3 v_lightVec2;

#ifdef GL_ES
varying highp float v_lightDistance1;
varying highp float v_lightDistance2;
highp vec3 bump;
highp float diffuse1;
highp float diffuse2;
highp float att;
highp vec4 diffuseColor1;
highp vec4 diffuseColor2;
#else
varying float v_lightDistance1;
varying float v_lightDistance2;
vec3 bump;
float diffuse1;
float diffuse2;
float att;
vec4 diffuseColor1;
vec4 diffuseColor2;
#endif

//varying vec4 v_color;

void main()
{
	//vec3 eVec = normalize(v_eyeVec);
	bump = texture2D(u_normals, v_texCoord).xyz * 2.0 -1.0;
	bump = normalize(bump);
	diffuseColor1 = vec4(0.0, 0.0, 0.0, 1.0);
	diffuseColor2 = vec4(0.0, 0.0, 0.0, 1.0);

	float distSqr = dot(v_lightVec1, v_lightVec1);	
	vec3 lVec = v_lightVec1 * inversesqrt(distSqr);
	diffuse1 = max(dot(lVec, bump), 0.0);
	att = 1.0 / (1.0 + (0.0001 * v_lightDistance1 * v_lightDistance1));
	diffuse1 = diffuse1;// * att;
	//diffuseColor.xyz += (diffuse * vec3(sqrt(u_lightsColor[0].x), sqrt(u_lightsColor[0].y), sqrt(u_lightsColor[0].z))) * att;
	diffuseColor1.xyz = u_color.xyz * diffuse1;

	distSqr = dot(v_lightVec2, v_lightVec2);	
	lVec = v_lightVec2 * inversesqrt(distSqr);
	diffuse2 = max(dot(lVec, bump), 0.0);
	att = 1.0 / (1.0 + (0.0001 * v_lightDistance2 * v_lightDistance2));
	diffuse2 = diffuse2;// * att;
	//diffuseColor.xyz += (diffuse2 * vec3(sqrt(u_lightsColor[1].x), sqrt(u_lightsColor[1].y), sqrt(u_lightsColor[1].z))) * att;
	diffuseColor2.xyz += u_color.xyz * diffuse2;

	if(diffuse1 > 0.95) {
		gl_FragColor = diffuseColor1;// + texture2D(u_texture, v_texCoord);
	} else {
		gl_FragColor = texture2D(u_texture, v_texCoord);
	}
	// float specular = pow(clamp(dot(reflect(-lVec, bump), eVec), 0.0, 1.0), 3.0);
	//if (diffuse1 > 0.95) {
	//	gl_FragColor = diffuseColor1 * 2.0;
	//} else if(diffuse2 > 0.95) {
	//	gl_FragColor = diffuseColor2 * 2.0;
	//} else {
	//	gl_FragColor = v_color * 0.4 * att;// + texture2D(u_texture, v_texCoord) * 0.1;
	//}
}
