#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;
uniform sampler2D u_texture;
uniform float blurSize;

void main() {
    vec4 color = vec4(0.0);
    float total = 0.0;
    // Sample 9 times in a 3x3 grid around the current pixel
    for (float x = -4.0; x <= 4.0; x += 1.0) {
        for (float y = -4.0; y <= 4.0; y += 1.0) {
            float weight = (9.0 - abs(x) - abs(y)) / 81.0; // Weighting
            color += texture2D(u_texture, v_texCoord + vec2(x, y) * blurSize) * weight;
            total += weight;
        }
    }
    gl_FragColor = color / total;
}
