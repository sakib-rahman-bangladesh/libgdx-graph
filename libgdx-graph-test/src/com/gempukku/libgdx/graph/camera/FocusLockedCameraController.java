package com.gempukku.libgdx.graph.camera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class FocusLockedCameraController implements CameraController {
    private Camera camera;
    private CameraFocus cameraFocus;
    private Vector2 cameraAnchor = new Vector2();

    private Vector2 tmpVector = new Vector2();

    public FocusLockedCameraController(Camera camera, CameraFocus cameraFocus) {
        this(camera, cameraFocus, new Vector2(0.5f, 0.5f));
    }

    public FocusLockedCameraController(Camera camera, CameraFocus cameraFocus, Vector2 focusAnchor) {
        this.camera = camera;
        this.cameraFocus = cameraFocus;
        this.cameraAnchor.set(focusAnchor);
    }

    @Override
    public void update(float delta) {
        Vector2 focus = cameraFocus.getFocus(tmpVector);
        camera.position.x = focus.x + camera.viewportWidth * (cameraAnchor.x - 0.5f);
        camera.position.y = focus.y + camera.viewportHeight * (cameraAnchor.y - 0.5f);
        camera.update();
    }
}
