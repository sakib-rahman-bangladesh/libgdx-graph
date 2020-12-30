package com.gempukku.libgdx.graph.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.gempukku.libgdx.graph.entity.GameEntity;
import com.gempukku.libgdx.graph.sprite.SpriteFaceDirection;
import com.gempukku.libgdx.graph.sprite.StateBasedSprite;
import com.gempukku.libgdx.graph.system.sensor.FootSensorData;

public class PlayerControlSystem implements GameSystem {
    private GameEntity<StateBasedSprite> playerEntity;

    public void setPlayerEntity(GameEntity<StateBasedSprite> playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public void update(float delta) {
        FootSensorData footSensorData = (FootSensorData) playerEntity.getSensorDataOfType("foot").getValue();
        boolean grounded = (footSensorData != null) && footSensorData.isGrounded();

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
        if (desiredHorizontalVelocity != 0 && grounded)
            playerSprite.setState("Walk");
        else if (verticalVelocity == 0)
            playerSprite.setState("Idle");

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && footSensorData != null && grounded) {
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().x, 14);
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
