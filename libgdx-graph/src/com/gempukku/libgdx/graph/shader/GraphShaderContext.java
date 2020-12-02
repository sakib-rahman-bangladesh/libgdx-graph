package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.time.TimeProvider;

public interface GraphShaderContext {
    PropertySource getPropertySource(String name);

    TimeProvider getTimeProvider();

    GraphShaderEnvironment getEnvironment();

    void addManagedResource(Disposable disposable);
}
