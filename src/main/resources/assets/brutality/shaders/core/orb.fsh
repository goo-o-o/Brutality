#version 150

uniform sampler2D Sampler0;
uniform float Time;
uniform float FillLevel;

in vec4 vertexColor;
in vec2 texCoord;

out vec4 fragColor;

// ----------------------- CONFIGURABLE PARAMETERS -----------------------
const float PIXEL_FACTOR      = 60.0;

const float CIRCLE_RADIUS   = 0.47;

const float BLOB_SCALE      = 4.0;
const float TIME_SCALE      = 0.35;

const float WAVE_FREQ       = 5.0;
const float WAVE_AMP        = 0.02;
const float WAVE_SPEED      = 1.0;

const float MENISCUS_THICK  = 0.25;
const float MENISCUS_FEATHER   = 0.25;

const float CENTER_GLOW_POW = 5.0;
const float CENTER_GLOW_INT = 0.75;

const float SURFACE_GLOW_THICK = 0.1;

const float UPPER_GLOW_BASE_OFFSET = 0.05;
const float UPPER_GLOW_FULL_OFFSET = 0.09;
const float UPPER_GLOW_BASE_RADIUS = 0.2;
const float UPPER_GLOW_EXTRA_RADIUS = 0.15;

// ----------------------- COLORS -----------------------
const vec3 COLOR_BRIGHT = vec3(0.031, 0.58, 0.961);
const vec3 COLOR_MID    = vec3(0.047, 0.427, 0.839);
const vec3 COLOR_BASE   = vec3(0.043, 0.275, 0.745);
const vec3 COLOR_DARK   = vec3(0.016, 0.075, 0.447);
const vec3 COLOR_SHEEN  = vec3(0.149, 0.824, 0.965);
// Float-based hash (good quality, works everywhere)
float hash(float n, float seed) {
    float x = sin(n + seed * 17.0) * 43758.5453;
    return fract(x);
}

// Gradient directions using hashed float
vec3 gradientDirection(float h) {
    int hi = int(mod(h * 16.0, 16.0));
    if (hi == 0) return vec3(1, 1, 0);
    if (hi == 1) return vec3(-1, 1, 0);
    if (hi == 2) return vec3(1, -1, 0);
    if (hi == 3) return vec3(-1, -1, 0);
    if (hi == 4) return vec3(1, 0, 1);
    if (hi == 5) return vec3(-1, 0, 1);
    if (hi == 6) return vec3(1, 0, -1);
    if (hi == 7) return vec3(-1, 0, -1);
    if (hi == 8) return vec3(0, 1, 1);
    if (hi == 9) return vec3(0, -1, 1);
    if (hi == 10) return vec3(0, 1, -1);
    if (hi == 11) return vec3(0, -1, -1);
    return vec3(0, -1, -1);
}

float perlin(vec3 p, float seed) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    f = f*f*(3.0-2.0*f);

    float v000 = dot(gradientDirection(hash(i.x + i.y*57.0 + i.z*113.0, seed)), f);
    float v100 = dot(gradientDirection(hash(i.x+1.0 + i.y*57.0 + i.z*113.0, seed)), f - vec3(1, 0, 0));
    float v010 = dot(gradientDirection(hash(i.x + (i.y+1.0)*57.0 + i.z*113.0, seed)), f - vec3(0, 1, 0));
    float v110 = dot(gradientDirection(hash(i.x+1.0 + (i.y+1.0)*57.0 + i.z*113.0, seed)), f - vec3(1, 1, 0));
    float v001 = dot(gradientDirection(hash(i.x + i.y*57.0 + (i.z+1.0)*113.0, seed)), f - vec3(0, 0, 1));
    float v101 = dot(gradientDirection(hash(i.x+1.0 + i.y*57.0 + (i.z+1.0)*113.0, seed)), f - vec3(1, 0, 1));
    float v011 = dot(gradientDirection(hash(i.x + (i.y+1.0)*57.0 + (i.z+1.0)*113.0, seed)), f - vec3(0, 1, 1));
    float v111 = dot(gradientDirection(hash(i.x+1.0 + (i.y+1.0)*57.0 + (i.z+1.0)*113.0, seed)), f - vec3(1, 1, 1));

    return mix(mix(mix(v000, v100, f.x), mix(v010, v110, f.x), f.y),
    mix(mix(v001, v101, f.x), mix(v011, v111, f.x), f.y), f.z);
}

float fbm(vec3 p, float seed) {
    float v = 0.0;
    float a = 0.5;
    float freq = 1.0;
    float s = seed;
    for (int i=0; i<3; i++) {
        s = hash(s, 3737.3737);// arbitrary
        v += a * perlin(p * freq + vec3(0.0, 0.0, Time * 0.03), s);
        freq *= 2.0;
        a *= 0.5;
    }
    return v;
}

void main() {
    // Center UV first
    vec2 centerUV = texCoord - 0.5;

    // Pixelate directly in UV space (resolution-independent)
    vec2 uv = floor(centerUV * PIXEL_FACTOR) / PIXEL_FACTOR;

    // Use pixelated uv for everything except blob positioning (keeps blobs smooth)
    float distToEdge = length(uv);

    if (distToEdge > CIRCLE_RADIUS) discard;

    float t = Time * TIME_SCALE;

    // Use original (non-pixelated) centerUV for blobs to avoid blocky noise
    vec2 p_blob = uv * BLOB_SCALE;

    vec3 pos1 = vec3(p_blob, t + t * 0.04);
    vec3 pos2 = vec3(p_blob + vec2(t*0.06, t*0.045), t + t * 0.036);
    vec3 pos3 = vec3(p_blob + vec2(t*0.072, t*0.066), t + t * 0.042);

    float n1 = fbm(pos1, 0.0) * 0.5 + 0.5;
    float n2 = fbm(pos2, 7.0) * 0.5 + 0.5;
    float n3 = fbm(pos3, 13.0) * 0.5 + 0.5;

    vec3 col = COLOR_BASE;

    float darkMasker = 1.0 - smoothstep(0.475, 0.525, n3);
    float midCore    = step(0.2, n2);
    float brightCore = step(0.35, n2 * n1) * darkMasker * midCore;
    float midMask    = step(0.4, n2);
    float darkCore   = step(0.55, n3);
    float darkMask   = step(0.45, n3);

    col = mix(col, COLOR_MID, midCore);
    col = mix(col, COLOR_BRIGHT, brightCore);
    col = mix(col, COLOR_BASE, midMask - midCore);
    col = mix(col, COLOR_DARK, darkCore);
    col = mix(col, COLOR_BASE, darkMask - darkCore);

    float centerGlow = pow(1.0 - distToEdge / CIRCLE_RADIUS, CENTER_GLOW_POW) * CENTER_GLOW_INT;
    col = mix(col, COLOR_BASE, centerGlow * 0.8);
    col += vec3(centerGlow * 0.1);

    float surfaceY = mix(-CIRCLE_RADIUS, CIRCLE_RADIUS, FillLevel);
    float waterY = uv.y;

    float displacement = 0.0;
    if (abs(waterY - surfaceY) < 0.25) {
        displacement = sin(uv.x * WAVE_FREQ + Time * WAVE_SPEED) * WAVE_AMP;
    }
    float surfaceLevel = surfaceY + displacement;
    float distToSurface = waterY - surfaceLevel;

    float edgeProx = smoothstep(CIRCLE_RADIUS - MENISCUS_FEATHER, CIRCLE_RADIUS, distToEdge);
    float meniscus = edgeProx * pow(1.0 - clamp(distToSurface / MENISCUS_THICK, 0.0, 1.0), 4.0);
    col = mix(col, vec3(0, 0, 0), meniscus * 0.5);

    float surfaceGlow = step(0.0, -distToSurface + 0.05) * (1.0 - smoothstep(0.0, SURFACE_GLOW_THICK, -distToSurface));
    col = mix(col, COLOR_BRIGHT, surfaceGlow * 0.7);

    float lowFill = 1.0 - FillLevel;
    float upperOffset = mix(UPPER_GLOW_BASE_OFFSET, UPPER_GLOW_FULL_OFFSET, lowFill);
    float upperRadius = UPPER_GLOW_BASE_RADIUS + lowFill * UPPER_GLOW_EXTRA_RADIUS;
    vec2 upperCenter = vec2(0.0, surfaceLevel + upperOffset);
    float upperDist = length(uv - upperCenter);
    float upperGlow = pow(1.0 - smoothstep(0.0, upperRadius, upperDist), 4.0) * 0.7;
    col = mix(col, COLOR_SHEEN, upperGlow * 0.9);
    col += COLOR_SHEEN * upperGlow * 0.2;

    float waterMask = step(waterY, surfaceLevel);
    float finalMask = max(waterMask, meniscus);

    vec3 finalCol = col * finalMask;
    float alpha = finalMask * vertexColor.a;

    if (alpha <= 0.9) discard;

    fragColor = vec4(finalCol, alpha);
}