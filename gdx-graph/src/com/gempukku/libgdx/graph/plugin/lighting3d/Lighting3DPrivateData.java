package com.gempukku.libgdx.graph.plugin.lighting3d;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class Lighting3DPrivateData implements Lighting3DPublicData, RuntimePipelinePlugin {
    private ObjectMap<String, Lighting3DEnvironment> environments = new ObjectMap<>();

    @Override
    public void setEnvironment(String id, Lighting3DEnvironment environment) {
        environments.put(id, environment);
    }

    @Override
    public Lighting3DEnvironment getEnvironment(String id) {
        return environments.get(id);
    }

    @Override
    public void update(TimeProvider timeProvider) {

    }
}
