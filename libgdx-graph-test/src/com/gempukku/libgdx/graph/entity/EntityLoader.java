package com.gempukku.libgdx.graph.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.gempukku.libgdx.graph.component.AnchorComponent;
import com.gempukku.libgdx.graph.component.FacingComponent;
import com.gempukku.libgdx.graph.component.PhysicsComponent;
import com.gempukku.libgdx.graph.component.PositionComponent;
import com.gempukku.libgdx.graph.component.SizeComponent;
import com.gempukku.libgdx.graph.component.SpriteComponent;
import com.gempukku.libgdx.graph.sprite.def.EntityDef;
import com.gempukku.libgdx.graph.sprite.def.PhysicsDef;
import com.gempukku.libgdx.graph.sprite.def.SpriteDef;

public class EntityLoader {
    public static Entity readEntity(Engine engine, Json json, String path) {
        EntityDef spriteDef = json.fromJson(EntityDef.class, Gdx.files.internal(path));

        return createEntity(engine, spriteDef);
    }

    private static Entity createEntity(Engine engine, EntityDef entityDef) {
        Entity entity = engine.createEntity();

        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        positionComponent.setPosition(entityDef.getPosition().x, entityDef.getPosition().y);

        SizeComponent sizeComponent = engine.createComponent(SizeComponent.class);
        sizeComponent.setSize(entityDef.getSize());

        AnchorComponent anchorComponent = engine.createComponent(AnchorComponent.class);
        anchorComponent.setAnchor(entityDef.getAnchor());

        entity.add(positionComponent).add(sizeComponent).add(anchorComponent);

        SpriteDef spriteDef = entityDef.getSpriteDef();
        if (spriteDef != null) {
            SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
            spriteComponent.setSpriteDef(spriteDef);

            entity.add(spriteComponent);

            if (spriteDef.getFacing() != null) {
                FacingComponent facingComponent = engine.createComponent(FacingComponent.class);
                facingComponent.setFaceDirection(spriteDef.getFacing());

                entity.add(facingComponent);
            }
        }

        PhysicsDef physicsDef = entityDef.getPhysicsDef();
        if (physicsDef != null) {
            PhysicsComponent physicsComponent = engine.createComponent(PhysicsComponent.class);
            physicsComponent.setPhysicsDef(physicsDef);

            entity.add(physicsComponent);
        }

        engine.addEntity(entity);

        return entity;
    }
}
