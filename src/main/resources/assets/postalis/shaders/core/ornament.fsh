#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float Opacity;
uniform float Time;

in vec2 texCoord0;
out vec4 fragColor;

void main() {
    vec2 uv = texCoord0;

    uv.y += sin(uv.x * 6.0 + Time * 0.5) * 0.01;
    uv.x += cos(uv.y * 6.0 + Time * 0.5) * 0.01;

    vec4 color = texture(Sampler0, uv);

    if (color.a < 0.01) {
        discard;
    }

    float wave = sin((uv.x + uv.y + Time * 0.3) * 10.0) * 0.5 + 0.5;
    float dynamicAlpha = Opacity * (0.6 + 0.4 * wave);

    fragColor = vec4(color.rgb * ColorModulator.rgb, color.a * ColorModulator.a * dynamicAlpha);
}