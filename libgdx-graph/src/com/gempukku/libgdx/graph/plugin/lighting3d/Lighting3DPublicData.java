package com.gempukku.libgdx.graph.plugin.lighting3d;

public interface Lighting3DPublicData {
    void setEnvironment(String id, Lighting3DEnvironment environment);

    Lighting3DEnvironment getEnvironment(String id);
}
