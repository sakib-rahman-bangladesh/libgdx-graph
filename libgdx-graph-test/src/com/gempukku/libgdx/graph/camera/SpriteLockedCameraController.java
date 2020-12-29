package com.gempukku.libgdx.graph.camera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.sprite.Sprite;

public class SpriteLockedCameraController implements CameraController {
    private Camera camera;
    private Sprite sprite;
    private Vector2 cameraAnchor = new Vector2();

    private Vector2 tmpVector = new Vector2();

    public SpriteLockedCameraController(Camera camera, Sprite sprite) {
        this(camera, sprite, new Vector2(0.5f, 0.5f));
    }

    public SpriteLockedCameraController(Camera camera, Sprite sprite, Vector2 spriteAnchor) {
        this.camera = camera;
        this.sprite = sprite;
        this.cameraAnchor.set(spriteAnchor);
    }

    @Override
    public void update(float delta) {
        sprite.getPosition(tmpVector);
        camera.position.set(tmpVector.x + camera.viewportWidth * (cameraAnchor.x - 0.5f), tmpVector.y + camera.viewportHeight * (cameraAnchor.y - 0.5f), camera.position.z);
        camera.update();
    }
}
