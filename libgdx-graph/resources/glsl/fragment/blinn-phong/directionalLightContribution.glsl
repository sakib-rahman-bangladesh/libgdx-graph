Lighting getDirectionalBlinnPhongLightContribution(vec3 pos, vec3 N, float shininess, Lighting lighting) {
    vec3 V = normalize(u_cameraPosition.xyz - pos.xyz);
    for (int i = 0; i < NUM_DIRECTIONAL_LIGHTS; i++) {
        vec3 L = -u_dirLights[i].direction;
        vec3 H = normalize(L + V);

        float diffuse = clamp(dot(N, L), 0.0, 1.0);
        float specular = pow(clamp(dot(H, N), 0.0, 1.0), shininess);

        lighting.diffuse += u_dirLights[i].color * diffuse;
        lighting.specular += (diffuse > 0.0 ? 1.0 : 0.0) * u_dirLights[i].color * specular;
    }
    return lighting;
}
