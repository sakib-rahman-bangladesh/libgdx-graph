vec3 applyBumpMap(vec3 tangent, vec3 normal, vec3 bump) {
    tangent = normalize(tangent);
    normal = normalize(normal);
    vec3 bitangent = normalize(cross(normal, tangent));
    mat3 TBN = mat3(tangent, bitangent, normal);
    return normalize(TBN * (bump * 2.0 - 1.0));
}
