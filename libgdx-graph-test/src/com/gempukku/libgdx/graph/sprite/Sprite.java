package com.gempukku.libgdx.graph.sprite;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;

public interface Sprite {
    SpriteFaceDirection getFaceDirection();

    Vector2 getPosition(Vector2 position);

    void setFaceDirection(SpriteFaceDirection faceDirection);

    void moveBy(float x, float y);

    boolean isDirty();

    void updateSprite(PipelineRenderer pipelineRenderer);
}
