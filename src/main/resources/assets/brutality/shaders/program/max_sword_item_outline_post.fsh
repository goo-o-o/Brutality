#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D OutlineSampler;
uniform sampler2D OutlineDepthSampler;
uniform vec2 OutSize;
uniform float Thickness;
uniform mat4 invViewMat;
uniform mat4 invProjMat;

in vec2 texCoord;
out vec4 fragColor;

vec3 worldPosFromDepth(vec2 uv) {
    float depth = texture(OutlineDepthSampler, uv).r;
    vec4 clipPos = vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    vec4 viewPos = invProjMat * clipPos;
    viewPos /= viewPos.w;
    vec4 worldPos = invViewMat * viewPos;
    return worldPos.xyz;
}

vec3 hexColor(float r, float g, float b) {
    return vec3(r / 255.0, g / 255.0, b / 255.0);
}

// 4-stop gradient — t from 0.0 to 1.0
vec3 gradient4(vec3 c0, vec3 c1, vec3 c2, vec3 c3, float t) {
    t = clamp(t, 0.0, 1.0);
    if (t < 0.333) return mix(c0, c1, t / 0.333);
    if (t < 0.666) return mix(c1, c2, (t - 0.333) / 0.333);
    return mix(c2, c3, (t - 0.666) / 0.334);
}

void main() {
    vec2 texel = 1.0 / OutSize;
    float center = texture(OutlineSampler, texCoord).a;

    if (center > 0.0) {
        fragColor = vec4(texture(DiffuseSampler, texCoord).rgb, 1.0);
        return;
    }

    int thickness = max(1, int(Thickness));
    float thicknessF = float(thickness);

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

    // Your 4 colors
    vec3 col0 = hexColor(0x5F, 0xEB, 0xBD); // #5febbd
    vec3 col1 = hexColor(0xBB, 0xFF, 0xF9); // #bbfff9
    vec3 col2 = hexColor(0xFF, 0x62, 0x35); // #ff6235
    vec3 col3 = hexColor(0xFF, 0xE8, 0x84); // #ffe884

    // Get world position from the closest silhouette pixel
    vec3 worldPos = worldPosFromDepth(closestUV);

    // Use world Y to drive gradient — adjust range to taste
    // 0.0 = col0 at Y=0, 1.0 = col3 at Y=64
    float t = fract(worldPos.y / 64.0);
    vec3 outlineColor = gradient4(col0, col1, col2, col3, t);

    float rawAlpha = texture(OutlineSampler, closestUV).a;
    float correctedAlpha = pow(rawAlpha, 2.2);
    vec3 world = texture(DiffuseSampler, texCoord).rgb;

    fragColor = vec4(mix(world, outlineColor, correctedAlpha), 1.0);
}