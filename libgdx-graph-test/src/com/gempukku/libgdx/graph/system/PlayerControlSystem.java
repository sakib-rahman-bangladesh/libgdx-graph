package com.gempukku.libgdx.graph.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.gempukku.libgdx.graph.GameSystem;
import com.gempukku.libgdx.graph.entity.GameEntity;
import com.gempukku.libgdx.graph.sprite.SpriteFaceDirection;
import com.gempukku.libgdx.graph.sprite.StateBasedSprite;

public class PlayerControlSystem implements GameSystem {
    private GameEntity<StateBasedSprite> playerEntity;

    public void setPlayerEntity(GameEntity<StateBasedSprite> playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public void update(float delta) {
        Body playerBody = playerEntity.getBody();
        StateBasedSprite playerSprite = playerEntity.getSprite();
        float desiredHorizontalVelocity = getDesiredHorizontalVelocity();

        float verticalVelocity = playerBody.getLinearVelocity().y;
        playerBody.setLinearVelocity(desiredHorizontalVelocity, verticalVelocity);

        if (desiredHorizontalVelocity > 0) {
            playerSprite.setFaceDirection(SpriteFaceDirection.Right);
        } else if (desiredHorizontalVelocity < 0) {
            playerSprite.setFaceDirection(SpriteFaceDirection.Left);
        }
        if (desiredHorizontalVelocity != 0 && verticalVelocity == 0)
            playerSprite.setState("Walk");
        else if (verticalVelocity == 0)
            playerSprite.setState("Idle");

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && verticalVelocity == 0f) {
            playerBody.applyLinearImpulse(0f, 14f, 0, 0, true);
            playerSprite.setState("Jump");
        }
    }

    private float getDesiredHorizontalVelocity() {
        if (isRightPressed() && !isLeftPressed())
            return 5;
        else if (isLeftPressed() && !isRightPressed())
            return -5;
        else
            return 0;
    }

    private boolean isRightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    private boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    @Override
    public void dispose() {

    }
}
