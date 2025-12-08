#version 150

uniform sampler2D DiffuseSampler;
uniform float Time;
uniform float Intensity;
uniform float Dissolve;
uniform vec4 BurnColor1;
uniform vec4 BurnColor2;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

// Simple dissolve with burn colors
vec4 applyDissolve(vec4 tex, float dissolve) {
    if (dissolve < 0.001) return tex;

    float noise = fract(sin(dot(texCoord0, vec2(12.9898, 78.233))) * 43758.5453);
    noise = (noise + fract(sin(dot(texCoord0 + vec2(1.0, 0.0), vec2(12.9898, 78.233))) * 43758.5453)) * 0.5;

    if (noise < dissolve) {
        if (noise > dissolve - 0.15 && BurnColor1.a > 0.0) {
            return BurnColor1;
        } else if (noise > dissolve - 0.3 && BurnColor2.a > 0.0) {
            return BurnColor2;
        }
        return vec4(0.0);
    }
    return tex;
}

// Foil/holographic effect
vec4 applyFoil(vec4 tex, float intensity) {
    if (intensity < 0.001) return tex;

    vec2 uv = texCoord0;
    float t = Time;

    // Create shifting rainbow patterns
    float r = sin(uv.x * 8.0 + t) * 0.5 + 0.5;
    float g = sin(uv.x * 6.0 + uv.y * 4.0 + t * 1.3) * 0.5 + 0.5;
    float b = sin(uv.y * 8.0 + t * 1.7) * 0.5 + 0.5;

    // Blend with original texture
    vec3 foilColor = vec3(r, g, b);
    tex.rgb = mix(tex.rgb, tex.rgb * 0.7 + foilColor * 0.3, intensity);

    // Add sparkles
    float sparkle = sin(uv.x * 50.0 + t * 5.0) * sin(uv.y * 40.0 + t * 4.0);
    sparkle = max(0.0, sparkle * 2.0 - 1.5);
    tex.rgb += sparkle * intensity * 0.4;

    // Saturation boost
    float gray = dot(tex.rgb, vec3(0.299, 0.587, 0.114));
    tex.rgb = mix(vec3(gray), tex.rgb, 1.0 + intensity * 0.5);

    return tex;
}

void main() {
    vec4 tex = texture(DiffuseSampler, texCoord0);

    // Apply foil effect
    tex = applyFoil(tex, Intensity);

    // Apply dissolve effect
    tex = applyDissolve(tex, Dissolve);

    fragColor = tex * vertexColor;
}