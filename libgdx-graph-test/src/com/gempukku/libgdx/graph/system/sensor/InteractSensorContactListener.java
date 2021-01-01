package com.gempukku.libgdx.graph.system.sensor;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.gempukku.libgdx.graph.entity.GameEntity;
import com.gempukku.libgdx.graph.entity.SensorData;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.sprite.Sprite;

public class InteractSensorContactListener implements SensorContactListener {
    private PipelineRenderer pipelineRenderer;

    public InteractSensorContactListener(PipelineRenderer pipelineRenderer) {
        this.pipelineRenderer = pipelineRenderer;
    }

    @Override
    public void contactBegun(SensorData sensor, Fixture other) {
        GameEntity entity = (GameEntity<?>) other.getUserData();
        Sprite sprite = entity.getSprite();
        pipelineRenderer.getGraphSprites().addTag(sprite.getGraphSprite(), "Outline");
    }

    @Override
    public void contactEnded(SensorData sensor, Fixture other) {
        GameEntity entity = (GameEntity<?>) other.getUserData();
        Sprite sprite = entity.getSprite();
        pipelineRenderer.getGraphSprites().removeTag(sprite.getGraphSprite(), "Outline");
    }
}
