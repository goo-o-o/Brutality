#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

uniform float PixelSize;
uniform vec2 InSize;

out vec4 fragColor;

void main() {
    // Calculate the pixelation factor
    vec2 pixelScale = InSize / PixelSize;

    // Quantize the texture coordinates
    vec2 pixelatedTexCoord = floor(texCoord * pixelScale) / pixelScale;

    // Sample the texture with the modified coordinates
    vec4 color = texture(DiffuseSampler, pixelatedTexCoord);

    fragColor = color;
}