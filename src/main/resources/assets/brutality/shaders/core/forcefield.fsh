#version 150

#moj_import <fog.glsl>

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;
in vec3 ViewDir;
in vec3 ViewNormal;

out vec4 fragColor;

float FresnelEffect(vec3 Normal, vec3 ViewDir, float power) {
    float cosTheta = clamp(dot(normalize(Normal), normalize(ViewDir)), 0, 1);
    return pow(1.0 - cosTheta, power);
}


void main() {
    float fresnelValue = FresnelEffect(ViewNormal, ViewDir, 0.5);
    vec4 color = linear_fog(vertexColor * ColorModulator, vertexDistance, FogStart, FogEnd, FogColor);
    fragColor = vec4(color.xyz, fresnelValue);

}