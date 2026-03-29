#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D OutlineSampler;
uniform vec2 OutSize;
uniform float Thickness;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 texel = 1.0 / OutSize;
    float center = texture(OutlineSampler, texCoord).a;

    // If inside the object, draw the world normally (no outline over the object)
    if (center > 0) {
        fragColor = vec4(texture(DiffuseSampler, texCoord).rgb, 1.0);
        return;
    }

    int thickness = max(1, int(Thickness));
    float thicknessF = float(thickness);

    // Pass 1: fast exit
    bool isEdge = false;
    for (int x = -thickness; x <= thickness && !isEdge; x++) {
        for (int y = -thickness; y <= thickness && !isEdge; y++) {
            if (x == 0 && y == 0) continue;
            if (length(vec2(float(x), float(y))) > thicknessF) continue;
            if (texture(OutlineSampler, clamp(texCoord + vec2(float(x), float(y)) * texel, 0.0, 1.0)).a > 0.1)
            isEdge = true;
        }
    }

    if (!isEdge) {
        fragColor = vec4(texture(DiffuseSampler, texCoord).rgb, 1.0);
        return;
    }

    // Pass 2: find closest for color sampling
    vec2 closestUV = texCoord;
    float minDist = 9999.0;
    for (int x = -thickness; x <= thickness; x++) {
        for (int y = -thickness; y <= thickness; y++) {
            if (x == 0 && y == 0) continue;
            float d = length(vec2(float(x), float(y)));
            if (d > thicknessF) continue;
            vec2 n = clamp(texCoord + vec2(float(x), float(y)) * texel, 0.0, 1.0);
            if (texture(OutlineSampler, n).a > 0.01 && d < minDist) {
                minDist = d;
                closestUV = n;
            }
        }
    }

    vec4 outlineSample = texture(OutlineSampler, closestUV);
    vec3 world = texture(DiffuseSampler, texCoord).rgb;

    // The raw alpha from Java (0.0 to 1.0)
    float rawAlpha = outlineSample.a;

//    float distanceFactor = 1.0 - clamp(minDist / (thicknessF + 0.5), 0.0, 1.0);
    // Apply Gamma Correction (standard 2.2 curve)
    // This pushes the "perceived" 0.5 back down to where it actually looks like 50%
    float correctedAlpha = pow(rawAlpha, 2.2);

    // Now mix using the corrected alpha
    vec3 blendedRGB = mix(world, outlineSample.rgb, correctedAlpha);

    fragColor = vec4(blendedRGB, 1.0);

}