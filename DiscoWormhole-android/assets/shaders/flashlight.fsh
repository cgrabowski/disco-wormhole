#ifdef GL_ES
precision mediump float;
#endif
varying vec2 v_texCoord;
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
vec4 color = texture2D(u_texture, v_texCoord.st);
vec3 nColor = texture2D(u_normals, v_texCoord.st);
// bump map intensity
vec3 nBase = vec3(0.5, 0.5, 1.0);
nColor = mix(nBase, nColor, strength);
//normals need to be converted to [-1.0, 1.0] range and normalized
vec3 normal = normalize(nColor * 2.0 - 1.0);
// distance calculation
vec3 deltaPos = vec3(light.xyz - gl_FragCoord.xyz);
vec3 lightDir = normalize(deltaPos);
float lambert = clamp(dot(normal, lightDir), 0.0, 1.0);
// falloff
float d = sqrt(dot(deltaPos, deltaPos));

vec3 result = (0.0, 0.0, 0.0) + (lightColor.rgb * lambert * 5.0);
result *= color.rgb;

gl_FragColor = vec4(result, color.a);
}
