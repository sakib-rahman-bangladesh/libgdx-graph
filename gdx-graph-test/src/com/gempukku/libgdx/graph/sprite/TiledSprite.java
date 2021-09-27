package com.gempukku.libgdx.graph.sprite;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.component.AnchorComponent;
import com.gempukku.libgdx.graph.component.PositionComponent;
import com.gempukku.libgdx.graph.component.SizeComponent;
import com.gempukku.libgdx.graph.component.SpriteComponent;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprites;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteUpdater;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class TiledSprite implements Sprite {
    private static Vector3 tmpPosition = new Vector3();
    private Entity entity;
    private TextureRegion textureRegion;
    private Vector2 tileRepeat = new Vector2();
    private boolean textureDirty = true;

    public TiledSprite(Entity entity, TextureRegion textureRegion, Vector2 tileRepeat) {
        this.entity = entity;
        this.textureRegion = textureRegion;
        this.tileRepeat.set(tileRepeat);
    }

    @Override
    public void updateSprite(TimeProvider timeProvider, PipelineRenderer pipelineRenderer) {
        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
        final PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        boolean positionDirty = positionComponent.isDirty();

        GraphSprites graphSprites = pipelineRenderer.getPluginData(GraphSprites.class);

        GraphSprite sprite = spriteComponent.getGraphSprite();
        if (positionDirty) {
            final SizeComponent sizeComponent = entity.getComponent(SizeComponent.class);
            final AnchorComponent anchorComponent = entity.getComponent(AnchorComponent.class);

            graphSprites.updateSprite(sprite,
                    new SpriteUpdater() {
                        @Override
                        public void processUpdate(Vector3 position) {
                            Vector3 tmpPosition = positionComponent.getPosition(TiledSprite.tmpPosition);
                            position.set(tmpPosition.x, tmpPosition.y, tmpPosition.z);
                        }
                    });
            graphSprites.setProperty(sprite, "Size", sizeComponent.getSize(new Vector2()));
            graphSprites.setProperty(sprite, "Anchor", anchorComponent.getAnchor(new Vector2()));

            positionComponent.clean();
        }

        if (textureDirty) {
            GraphSprite graphSprite = sprite;
            graphSprites.setProperty(graphSprite, "Tile Texture", textureRegion);
            graphSprites.setProperty(graphSprite, "Tile Repeat", tileRepeat);
        }
        textureDirty = false;
    }
}
