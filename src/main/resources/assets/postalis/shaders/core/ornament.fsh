#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float Opacity;
uniform float Time; // добавляем время

in vec2 texCoord0;
out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0);

    if (color.a == 0.0) {
        discard;
    }

    // Вычисляем фазу волны по координатам и времени
    float wave = sin((texCoord0.y + texCoord0.x) * 10.0 - Time * 3.0) * 0.5 + 0.5;

    float dynamicAlpha = Opacity * wave;

    fragColor = vec4(color.rgb * ColorModulator.rgb, color.a * ColorModulator.a * dynamicAlpha);
}