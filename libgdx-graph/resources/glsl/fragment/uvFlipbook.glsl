vec2 uvFlipbook(vec2 uv, float width, float height, float index, float looping, vec2 invert)
{
    float spriteCount = width * height;
    float indexAdjusted = mix(min(index, spriteCount - 1.0), index, step(1.0, looping));
    float spriteIndex = floor(mod(indexAdjusted, spriteCount));
    vec2 inverseSpriteCount = vec2(1.0) / vec2(width, height);
    float tileX = abs(invert.x * width - ((spriteIndex - width * floor(spriteIndex * inverseSpriteCount.x)) + invert.x * 1.0));
    float tileY = abs(invert.y * height - (floor(spriteIndex * inverseSpriteCount.x) + invert.y * 1.0));
    return (uv + vec2(tileX, tileY)) * inverseSpriteCount;
}
