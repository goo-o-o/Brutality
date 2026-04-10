#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D PixelateSampler;
uniform vec2 OutSize;
uniform float PixelSize;
uniform float Spread;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    float spreadFactor = Spread;
    float spreadSamples = 0.0;

    // 1. Check the PixelateSampler (the 'final' buffer) ONLY for alpha
    for (float sx = -spreadFactor; sx <= spreadFactor; sx += 1.0) {
        for (float sy = -spreadFactor; sy <= spreadFactor; sy += 1.0) {
            vec2 spreadOffset = vec2(sx, sy) / OutSize;
            if (texture(PixelateSampler, texCoord + spreadOffset).a > 0.01) {
                spreadSamples += 1.0;
            }
        }
    }

    if (spreadSamples > 0.0) {
        vec2 pixelatedCoord = floor(texCoord * OutSize / PixelSize) * PixelSize / OutSize;

        // Sample the background scene (DiffuseSampler)
        vec4 sceneColor = texture(DiffuseSampler, pixelatedCoord);

        // Opaque output replaces the 'stencil' pixels with pixelated world pixels
        fragColor = vec4(sceneColor.rgb, 1.0);
    } else {
        fragColor = texture(DiffuseSampler, texCoord);
    }
}