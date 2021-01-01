package com.gempukku.libgdx.graph.sprite;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.time.TimeProvider;

public interface Sprite {
    GraphSprite getGraphSprite();

    Vector2 getPosition(Vector2 position);

    Vector2 getSize(Vector2 size);

    Vector2 getAnchor(Vector2 anchor);

    void setPosition(float x, float y);

    boolean isDirty();

    void updateSprite(TimeProvider timeProvider, PipelineRenderer pipelineRenderer);
}
