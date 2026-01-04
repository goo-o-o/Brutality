#version 330 core

uniform float time;
uniform float intensity;
uniform vec4 colour_1;
uniform vec4 colour_2;

in vec2 TexCoord;
out vec4 FragColor;

#define PIXEL_SIZE_FAC 60.0

void main()
{
    if (intensity < 0.1) discard;

    vec2 uv = vec2(TexCoord.x - 0.5, (TexCoord.y - 0.3) - 0.5);

    // ─── PIXELATE EVERYTHING FROM THE START ─────────────────────
    vec2 pixelUV = floor(uv * PIXEL_SIZE_FAC) / PIXEL_SIZE_FAC;   // this is the only UV we ever use now
    pixelUV += pixelUV * 0.01 * sin(-1.123 * pixelUV.x + 0.2 * time) * cos(5.3332 * pixelUV.y + time * 0.931);

    // use pixelUV for the flame distortion too
    vec2 flameUp = vec2(0.0, mod(4.0 * time, 10000.0) - 5000.0);

    float scale = 7.5 + 3.0 / (2.0 + 2.0 * intensity);
    vec2 sv = pixelUV * scale + flameUp;

    float speed = 20.781 + sin(time) * cos(time * 0.151);
    vec2 sv2 = vec2(0.0);

    for (int i = 0; i < 5; i++) {
        sv2 += sv + 0.05 * sv2.yx * (mod(float(i),2.0)>1.0 ? -1.0 : 1.0)
        + 0.3 * (cos(length(sv)*0.411) + 0.3344*sin(length(sv)) - 0.23*cos(length(sv)));
        sv += 0.5 * vec2(
        cos(cos(sv2.y) + speed*0.0812) * sin(3.22 + sv2.x - speed*0.1531),
        sin(-sv2.x*1.21222 + 0.113785*speed) * cos(sv2.y*0.91213 - 0.13582*speed)
        );
    }

    float smoke = max(0.0, (length((sv - flameUp)/scale*5.0) + 0.1*(length(pixelUV)-0.5)) * 2.0/(2.0+intensity*0.2));
    smoke += max(0.0, 2.0 - 0.3*intensity) * max(0.0, 2.0*(pixelUV.y-0.5)*(pixelUV.y-0.5));

    // ← ALL edge checks now use the same pixelUV → perfect crisp edges
    if (abs(pixelUV.x) > 0.55) smoke += 10.0 * (abs(pixelUV.x) - 0.4);

    if (length((uv - vec2(0.0,0.1))*vec2(0.19,1.0)) < min(0.1,intensity*0.5) && smoke > 1.0)
    smoke += min(8.5,intensity*10.0) * (length((uv - vec2(0.0,0.1))*vec2(0.19,1.0))-0.1);

    if (smoke > 1.0) discard;

    vec4 colour = colour_1;

    if (uv.y < 0.12) {
        float t = 0.12 - uv.y;
        colour = colour * (1.0 - 0.5 * t) + 2.5 * t * colour_2;
        colour += colour * (-2.0 + 0.5 * intensity * smoke) * t;
    }

    FragColor = colour;
}