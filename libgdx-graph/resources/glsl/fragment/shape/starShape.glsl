float getStarValue(vec2 vec, float arms, float min, float max, float power) {
    vec2 vectorFromMiddle = normalize(vec - vec2(0.5, 0.5));
    // Arccosine node
    float topPartAngle = acos(vectorFromMiddle.x);
    // Step node
    float isBottomMultiplier = step(0.0, vectorFromMiddle.y);
    // Arccosine node
    float bottomAngle = acos(-vectorFromMiddle.x);
    // Add node
    float angle = topPartAngle + isBottomMultiplier * bottomAngle * 2.0;
    float anglePerArm = 2.0 * 3.14159265358979323846 / arms;
    float inArm = mod(angle, anglePerArm) / anglePerArm;
    return min + (max - min) * pow(2.0 * abs(inArm-0.5), power);
}