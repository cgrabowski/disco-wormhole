uniform mat4 u_matModelViewProjection;
uniform vec3 light;
attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;
varying vec2 v_texCoord;
varying vec3 v_lightVec;
void main()
{
// attempting to create a xform matrix
vec3 tangent;
vec3 bitangent;
// calculate tangent
vec3 c1 = cross(a_normal, vec3(1.0, 0.0, 0.0));
vec3 c2 = cross(a_normal, vec3(0.0, 0.0, 1.0));

if(length(c1) > length(c2)) {
	tangent = c1;
} else {
	tangent = c2;
}

tangent = normalize(tangent);
bitangent = cross(a_normal, tangent);
bitangent = normalize(bitangent);

// create transformation matrix
mat3 trans = mat3(tangent, a_normal, bitangent);

// calculate light direction in tangent space from light pos in world space
vec3 lightDir = normalize(light);

v_lightVec = lightDir * trans;
v_texCoord = a_texCoord0;
gl_Position = u_matModelViewProjection * a_position;
}   
