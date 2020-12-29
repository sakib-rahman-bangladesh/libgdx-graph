package com.gempukku.libgdx.graph.sprite;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;

public interface Sprite {
    Vector2 getPosition(Vector2 position);

    Vector2 getSize(Vector2 size);

    Vector2 getAnchor(Vector2 anchor);

    void setPosition(float x, float y);

    boolean isDirty();

    void updateSprite(PipelineRenderer pipelineRenderer);
}
