package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;

public interface RenderableModel {
    Vector3 getPosition();

    Matrix4 getWorldTransform();

    Matrix4[] getBones();

    boolean isRendered(Camera camera);

    PropertyContainer getPropertyContainer();

    void render(Camera camera, ModelGraphShader shader);
}
