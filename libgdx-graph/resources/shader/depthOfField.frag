#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_sourceTexture;
uniform sampler2D u_depthTexture;

uniform vec2 u_pixelSize;
uniform int u_vertical;
uniform vec2 u_cameraClipping;
uniform vec2 u_focusDistance;
uniform float u_nearDistanceBlur;
uniform float u_farDistanceBlur;

varying vec2 v_position;

const float PI = 3.14159265358979323846;
const float SIGMA_HELPER = 3.5676;

float kernel[MAX_BLUR];

float unpackVec3ToFloat(vec3 packedValue)
{
    float packScale = u_cameraClipping.y;
    float result = dot(packedValue, 1.0 / vec3(1.0, 256.0, 256.0 * 256.0));
    return packScale * result * (256.0 * 256.0 * 256.0) / (256.0 * 256.0 * 256.0 - 1.0);
}

void initializeKernel(int blur) {
    for (int i=0; i <= MAX_BLUR; i++) {
        kernel[i] = 0.0;
    }

    float sigma = float(blur) / SIGMA_HELPER;
    float norm = 1.0 / (sqrt(2.0 * PI) * sigma);
    float coeff = 2.0 * sigma * sigma;
    float total = 0.0;
    for (int i = 0; i <= MAX_BLUR; i++) {
        if (i > blur) {
            break;
        }
        float value = norm * exp(-float(i) * float(i) / coeff);
        kernel[i] = value;
        total += value;
        if (i > 0) {
            total += value;
        }
    }
    for (int i = 0; i <= MAX_BLUR; i++) {
        if (i > blur) {
            break;
        }
        kernel[i] = kernel[i] / total;
    }
}

float getDepth() {
    vec4 depthColor = texture2D(u_depthTexture, v_position);
    return unpackVec3ToFloat(depthColor.rgb);
}

float getDeclaredBlur() {
    float depth = getDepth();
    if (depth + 0.00001 > u_cameraClipping.y) {
        return 0.0;
    } else if (u_focusDistance.x <= depth && depth <= u_focusDistance.y) {
        return 0.0;
    } else if (depth < u_focusDistance.x) {
        // It's too close
        return u_nearDistanceBlur * (u_focusDistance.x - depth) / (u_focusDistance.x - u_cameraClipping.x);
    } else {
        // It's too far
        return u_farDistanceBlur * (depth - u_focusDistance.y) / (u_cameraClipping.y - u_focusDistance.y);
    }
}

int getBlur() {
    return int(min(getDeclaredBlur(), float(MAX_BLUR)));
}

void main() {
    vec4 sampleAccum = vec4(0.0, 0.0, 0.0, 0.0);

    int blur = getBlur();

    initializeKernel(blur);
    if (blur == 0) {
        kernel[0] = 1.0;
    }

    for (int i = 0; i <= MAX_BLUR; i++) {
        if (i > blur) {
            break;
        }
        float kernelValue = kernel[i];
        if (u_vertical == 1) {
            sampleAccum += texture2D(u_sourceTexture, v_position + u_pixelSize * vec2(0, i)) * kernelValue;
            if (i > 0) {
                sampleAccum += texture2D(u_sourceTexture, v_position - u_pixelSize * vec2(0, i)) * kernelValue;
            }
        } else {
            sampleAccum += texture2D(u_sourceTexture, v_position + u_pixelSize * vec2(i, 0)) * kernelValue;
            if (i > 0) {
                sampleAccum += texture2D(u_sourceTexture, v_position - u_pixelSize * vec2(i, 0)) * kernelValue;
            }
        }
    }

    gl_FragColor = sampleAccum;
}