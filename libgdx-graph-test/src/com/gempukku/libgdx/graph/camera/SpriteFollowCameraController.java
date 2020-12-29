package com.gempukku.libgdx.graph.camera;

import com.badlogic.gdx.graphics.Camera;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;

public class SpriteFollowCameraController implements CameraController {
    public Camera camera;
    public GraphSprite sprite;

    public SpriteFollowCameraController(Camera camera, GraphSprite sprite) {
        this.camera = camera;
        this.sprite = sprite;
    }

    @Override
    public void update(float delta) {

    }
}
