package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;

public interface RenderableModel {
    Vector3 getPosition();

    boolean isRendered(Camera camera);

    Matrix4 getWorldTransform(String tag);

    Matrix4[] getBones(String tag);

    PropertyContainer getPropertyContainer(String tag);

    void render(Camera camera, ModelGraphShader shader);
}
