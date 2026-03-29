#version 150

uniform sampler2D Sampler0;

in vec2 texCoord0;
in vec4 vertColor;
out vec4 fragColor;

void main() {
    vec4 tex = texture(Sampler0, texCoord0);

    // Instead of a hard discard, multiply the texture alpha by the vertex alpha.
    // This preserves the "softness" of the item's edge.
    float alpha = tex.a * vertColor.a;

    if (alpha < 0.01) discard; // Only discard truly invisible pixels

    fragColor = vec4(vertColor.rgb, alpha);
}