// Directions - left, right, up, down
vec4 borderDetection(sampler2D texture, vec4 uvTransform, vec2 uv, vec2 pixelSize, vec4 directions, float outlineWidth, float alphaEdge) {
    float alphaResult = 1.0 - step(alphaEdge, texture2D(texture, uvTransform.xy + uv * uvTransform.zw).a);

    float leftResult = 0.0;
    float rightResult = 0.0;
    float upResult = 0.0;
    float downResult = 0.0;
    if (directions.x == 1.0) {
        leftResult = alphaResult * step(1.001 - alphaEdge, texture2D(texture, uvTransform.xy + (uv + outlineWidth * vec2(pixelSize.x, 0.0)) * uvTransform.zw).a);
    }
    if (directions.y == 1.0) {
        rightResult = alphaResult * step(1.001 - alphaEdge, texture2D(texture, uvTransform.xy + (uv + outlineWidth * vec2(-pixelSize.x, 0.0)) * uvTransform.zw).a);
    }
    if (directions.z == 1.0) {
        upResult = alphaResult * step(1.001 - alphaEdge, texture2D(texture, uvTransform.xy + (uv + outlineWidth * vec2(0.0, pixelSize.y)) * uvTransform.zw).a);
    }
    if (directions.w == 1.0) {
        downResult = alphaResult * step(1.001 - alphaEdge, texture2D(texture, uvTransform.xy + (uv + outlineWidth * vec2(0.0, -pixelSize.y)) * uvTransform.zw).a);
    }

    return vec4(leftResult, rightResult, upResult, downResult);
}
