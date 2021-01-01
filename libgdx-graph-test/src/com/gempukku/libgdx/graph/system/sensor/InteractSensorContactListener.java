package com.gempukku.libgdx.graph.system.sensor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.gempukku.libgdx.graph.component.SpriteComponent;
import com.gempukku.libgdx.graph.entity.SensorData;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;

public class InteractSensorContactListener implements SensorContactListener {
    private PipelineRenderer pipelineRenderer;

    public InteractSensorContactListener(PipelineRenderer pipelineRenderer) {
        this.pipelineRenderer = pipelineRenderer;
    }

    @Override
    public void contactBegun(SensorData sensor, Fixture other) {
        Entity entity = (Entity) other.getUserData();
        SpriteComponent sprite = entity.getComponent(SpriteComponent.class);
        pipelineRenderer.getGraphSprites().addTag(sprite.getGraphSprite(), "Outline");
    }

    @Override
    public void contactEnded(SensorData sensor, Fixture other) {
        Entity entity = (Entity) other.getUserData();
        SpriteComponent sprite = entity.getComponent(SpriteComponent.class);
        pipelineRenderer.getGraphSprites().removeTag(sprite.getGraphSprite(), "Outline");
    }
}
