#ifdef GL_ES
precision mediump float;
#endif
varying vec2 v_texCoord;
varying vec3 v_lightVec;

uniform sampler2D u_texture;
uniform sampler2D u_normals;
uniform float strength;
uniform vec3 light;
uniform vec3 lightColor;
//uniform vec2 resolution;
uniform vec4 u_color;
void main()
{
// sample color & normals from textures
vec4 color = texture2D(u_texture, v_texCoord);
vec3 nColor = texture2D(u_normals, v_texCoord).rgb;
//normals need to be converted to [-1.0, 1.0] range
vec3 normal = nColor * 2.0 - 1.0;

float lightIntensity = dot(v_lightVec, normal);

vec3 result = (0.0, 0.0, 0.0) + (lightColor.rgb * lightIntensity);
result *= color.rgb;

gl_FragColor = vec4(result, color.a);
}
